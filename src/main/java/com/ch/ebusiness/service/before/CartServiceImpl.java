package com.ch.ebusiness.service.before;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.ch.ebusiness.entity.Goods;
import com.ch.ebusiness.entity.Order;
import com.ch.ebusiness.repository.before.CartRepository;
import com.ch.ebusiness.repository.before.IndexRepository;
import com.ch.ebusiness.util.MD5Util;
import com.ch.ebusiness.util.MoneyUtil;
import com.ch.ebusiness.util.MyUtil;

@Service
public class CartServiceImpl implements CartService {

	private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
	@Autowired
	private com.ch.ebusiness.repository.before.UserRepository userRepository;

	@org.springframework.beans.factory.annotation.Autowired
	private org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder passwordEncoder;

	/**
	 * 安全的密码修改方法 - 只能修改当前登录用户的密码
	 */
	@Override
	public String updatePasswordSecure(String currentUserEmail, String newPassword) {
		// 校验当前用户是否存在
		com.ch.ebusiness.entity.BUser user = userRepository.selectByEmail(currentUserEmail);
		if (user == null) {
			return "用户不存在";
		}
		// 加密新密码（使用 BCrypt）
		String encodedPwd = passwordEncoder.encode(newPassword);
		// 更新数据库
		int updated = userRepository.updatePasswordByEmail(currentUserEmail, encodedPwd);
		if (updated == 0) {
			return "密码修改失败";
		}
		return null; // 成功
	}

	/**
	 * 旧版本密码修改方法（保留兼容）
	 */
	@Override
	@Deprecated
	public String updatePassword(HttpSession session, String bemail, String newPassword) {
		// 校验邮箱是否存在
		com.ch.ebusiness.entity.BUser user = userRepository.selectByEmail(bemail);
		if (user == null) {
			return "邮箱不存在";
		}
		// 加密新密码
		String encodedPwd = passwordEncoder.encode(newPassword);
		// 更新数据库
		int updated = userRepository.updatePasswordByEmail(bemail, encodedPwd);
		if (updated == 0) {
			return "密码修改失败";
		}
		// 清除session，强制重新登录
		session.invalidate();
		return null;
	}

	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private IndexRepository indexRepository;
	@Autowired
	private com.ch.ebusiness.util.RedisLockUtil redisLockUtil;
	@Autowired
	private org.springframework.data.redis.core.RedisTemplate<String, Object> redisTemplate;

	@Override
	public String putCart(Goods goods, Model model, HttpSession session) {
		Integer uid = MyUtil.getUser(session).getId();
		// 如果商品已在购物车，只更新购买数量
		if (cartRepository.isPutCart(uid, goods.getId()).size() > 0) {
			cartRepository.updateCart(uid, goods.getId(), goods.getBuyNumber());
		} else {// 新增到购物车
			cartRepository.putCart(uid, goods.getId(), goods.getBuyNumber());
		}
		// 跳转到查询购物车
		return "forward:/cart/selectCart";
	}

	@Override
	public String selectCart(Model model, HttpSession session, String act) {
		List<Map<String, Object>> list = cartRepository.selectCart(MyUtil.getUser(session).getId());
		BigDecimal sum = BigDecimal.ZERO;
		for (Map<String, Object> map : list) {
			Object smallsumObj = map.get("smallsum");
			BigDecimal smallsum = (smallsumObj instanceof BigDecimal)
					? (BigDecimal) smallsumObj
					: BigDecimal.valueOf(((Number) smallsumObj).doubleValue());
			sum = sum.add(smallsum);
		}
		model.addAttribute("total", MoneyUtil.format(sum));
		model.addAttribute("cartlist", list);
		// // 广告区商品
		// model.addAttribute("advertisementGoods",
		// indexRepository.selectAdvertisementGoods());
		// 导航栏商品类型
		model.addAttribute("goodsType", indexRepository.selectGoodsType());
		if ("toCount".equals(act)) {// 去结算页面
			return "user/count";
		}
		return "user/cart";
	}

	@Override
	public String deleteCart(HttpSession session, Integer gid) {
		Integer uid = MyUtil.getUser(session).getId();
		cartRepository.deleteAgoods(uid, gid);
		return "forward:/cart/selectCart";
	}

	@Override
	public String clearCart(HttpSession session) {
		cartRepository.clear(MyUtil.getUser(session).getId());
		return "forward:/cart/selectCart";
	}

	@Override
	public String submitOrder(Order order, Model model, HttpSession session) {
		// 最多重试3次
		int maxRetries = 3;
		for (int i = 0; i < maxRetries; i++) {
			try {
				return submitOrderWithLock(order, model, session);
			} catch (RuntimeException e) {
				if (e.getMessage().contains("库存不足") || i == maxRetries - 1) {
					throw e; // 库存不足或最后一次重试失败，直接抛出异常
				}
				// 并发冲突，等待后重试
				try {
					Thread.sleep(50 * (i + 1)); // 递增等待时间
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					throw new RuntimeException("下单被中断");
				}
			}
		}
		throw new RuntimeException("下单失败，请重试");
	}

	/**
	 * 使用分布式锁的下单方法
	 * 使用READ_COMMITTED隔离级别，提高并发性能
	 */
	@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
	private String submitOrderWithLock(Order order, Model model, HttpSession session) {
		long startTime = System.currentTimeMillis();
		Integer userId = MyUtil.getUser(session).getId();
		order.setBusertable_id(userId);

		logger.info("用户[{}]开始提交订单，订单金额：{}", userId, order.getAmount());

		// 防止重复提交：检查最近3秒内是否有相同金额的订单
		String orderIdempotentKey = "order:idempotent:" + userId + ":" + order.getAmount();
		Boolean isFirstSubmit = redisTemplate.opsForValue().setIfAbsent(
				orderIdempotentKey,
				"1",
				3,
				java.util.concurrent.TimeUnit.SECONDS);
		if (!Boolean.TRUE.equals(isFirstSubmit)) {
			throw new RuntimeException("请勿重复提交订单");
		}

		try {
			// 1. 获取购物车商品列表
			List<Map<String, Object>> listGoods = cartRepository.selectGoodsShop(userId);
			if (listGoods == null || listGoods.isEmpty()) {
				throw new RuntimeException("购物车为空");
			}

			// 2. 为每个商品获取分布式锁并检查库存
			String requestId = com.ch.ebusiness.util.RedisLockUtil.generateRequestId();
			java.util.List<String> acquiredLocks = new java.util.ArrayList<>();

			try {
				// 2.1 获取所有商品的锁
				for (Map<String, Object> map : listGoods) {
					Integer gid = (Integer) map.get("gid");
					Integer gshoppingnum = (Integer) map.get("gshoppingnum");

					String lockKey = "goods:stock:lock:" + gid;

					// 尝试获取锁，最多等待2秒
					boolean locked = redisLockUtil.tryLock(lockKey, requestId, 5);
					if (!locked) {
						throw new RuntimeException("商品正在被其他用户购买，请稍后重试");
					}
					acquiredLocks.add(lockKey);

					// 2.2 检查库存
					Goods goods = indexRepository.selectAGoods(gid);
					if (goods == null) {
						throw new RuntimeException("商品不存在，商品ID：" + gid);
					}
					if (goods.getStock() < gshoppingnum) {
						throw new RuntimeException(
								"商品【" + goods.getGname() + "】库存不足！当前库存：" + goods.getStock() + "，需要：" + gshoppingnum);
					}
				}

				// 3. 生成订单
				cartRepository.addOrder(order);

				// 4. 生成订单详情
				cartRepository.addOrderDetail(order.getId(), userId);

				// 5. 批量扣减库存（使用数据库悲观锁）
				for (Map<String, Object> map : listGoods) {
					int updatedRows = cartRepository.updateStore(map);
					if (updatedRows == 0) {
						// 库存扣减失败，回滚事务
						Integer gid = (Integer) map.get("gid");
						Goods goods = indexRepository.selectAGoods(gid);
						throw new RuntimeException("商品【" + goods.getGname() + "】库存不足，下单失败！");
					}
				}

				// 6. 清空购物车
				cartRepository.clear(userId);

				long duration = System.currentTimeMillis() - startTime;
				logger.info("用户[{}]订单提交成功，订单ID：{}，耗时：{}ms", userId, order.getId(), duration);

				model.addAttribute("order", order);
				return "user/pay";

			} finally {
				// 释放所有获取的锁
				for (String lockKey : acquiredLocks) {
					redisLockUtil.releaseLock(lockKey, requestId);
				}
			}
		} catch (Exception e) {
			// 删除幂等性key，允许重试
			redisTemplate.delete(orderIdempotentKey);
			throw e;
		}
	}

	@Override
	public String pay(Order order) {
		cartRepository.pay(order.getId());
		return "ok";
	}

	@Override
	public String myOder(Model model, HttpSession session) {
		return myOder(model, session, 1);
	}

	@Override
	public String myOder(Model model, HttpSession session, Integer page) {
		// 导航栏商品类型
		model.addAttribute("goodsType", indexRepository.selectGoodsType());

		Integer userId = MyUtil.getUser(session).getId();

		// 分页参数
		int pageSize = 10; // 每页显示10条
		int currentPage = (page == null || page < 1) ? 1 : page;
		int startIndex = (currentPage - 1) * pageSize;

		// 获取总订单数
		int totalOrders = cartRepository.countMyOrder(userId);
		int totalPages = (int) Math.ceil((double) totalOrders / pageSize);

		// 确保当前页不超过总页数
		if (currentPage > totalPages && totalPages > 0) {
			currentPage = totalPages;
			startIndex = (currentPage - 1) * pageSize;
		}

		// 获取分页数据
		List<Map<String, Object>> orders = cartRepository.myOrderByPage(userId, startIndex, pageSize);

		model.addAttribute("myOrder", orders);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("totalOrders", totalOrders);

		return "user/myOrder";
	}

	@Override
	public String orderDetail(Model model, Integer id) {
		model.addAttribute("orderDetail", cartRepository.orderDetail(id));
		return "user/orderDetail";
	}

	@Override
	public String updateUpwd(HttpSession session, String bpwd) {
		Integer uid = MyUtil.getUser(session).getId();
		cartRepository.updateUpwd(uid, MD5Util.MD5(bpwd));
		return "forward:/user/toLogin";
	}

}
