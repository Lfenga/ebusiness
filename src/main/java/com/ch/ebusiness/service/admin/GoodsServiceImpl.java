package com.ch.ebusiness.service.admin;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.ch.ebusiness.entity.Goods;
import com.ch.ebusiness.repository.admin.GoodsRepository;
import com.ch.ebusiness.service.MultiLevelCacheService;
import com.ch.ebusiness.util.MyUtil;

@Service
public class GoodsServiceImpl implements GoodsService {
	private static final Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class);

	@Autowired
	private GoodsRepository goodsRepository;

	@Autowired
	private MultiLevelCacheService cacheService;

	@Override
	public String selectAllGoodsByPage(Model model, int currentPage, String act) {
		// 共多少个商品
		int totalCount = goodsRepository.selectAllGoods();
		// 计算共多少页
		int pageSize = 5;
		int totalPage = (int) Math.ceil(totalCount * 1.0 / pageSize);
		List<Goods> typeByPage = goodsRepository.selectAllGoodsByPage((currentPage - 1) * pageSize, pageSize);
		model.addAttribute("allGoods", typeByPage);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("act", act);
		return "admin/selectGoods";
	}

	@Override
	public String addGoods(Goods goods, HttpServletRequest request, String act)
			throws IllegalStateException, IOException {
		// 验证库存不能为负数
		if (goods.getStock() < 0) {
			throw new IllegalArgumentException("商品库存不能小于0！");
		}

		MultipartFile myfile = goods.getFileName();
		// 如果选择了上传文件，将文件上传到指定的目录images
		if (!myfile.isEmpty()) {
			// 获取项目根目录（动态获取，适配不同环境）
			String projectPath = System.getProperty("user.dir");
			String path = projectPath + File.separator + "src" + File.separator + "main" +
					File.separator + "resources" + File.separator + "static" + File.separator + "images";

			// 获得上传文件原名
			String fileName = myfile.getOriginalFilename();
			// 对文件重命名
			String fileNewName = MyUtil.getNewFileName(fileName);
			File filePath = new File(path + File.separator + fileNewName);

			// 如果文件目录不存在，创建目录
			if (!filePath.getParentFile().exists()) {
				filePath.getParentFile().mkdirs();
				logger.info("创建图片目录: {}", filePath.getParentFile().getAbsolutePath());
			}

			// 将上传文件保存到一个目标文件中
			myfile.transferTo(filePath);
			logger.info("图片上传成功: {}, 路径: {}", fileNewName, filePath.getAbsolutePath());

			// 将重命名后的图片名存到goods对象中，添加时使用
			goods.setGpicture(fileNewName);
		}
		if ("add".equals(act)) {
			int n = goodsRepository.addGoods(goods);
			if (n > 0) {// 成功
				// 清除相关缓存
				clearGoodsRelatedCache(goods.getId(), goods.getGoodstype_id());
				logger.info("新增商品成功，已清除相关缓存，商品ID: {}", goods.getId());
				return "redirect:/goods/selectAllGoodsByPage?currentPage=1&act=select";
			}
			// 失败
			return "admin/addGoods";
		} else {// 修改 (act=update)
			int n = goodsRepository.updateGoods(goods);
			if (n > 0) {// 成功
				// 清除相关缓存
				clearGoodsRelatedCache(goods.getId(), goods.getGoodstype_id());
				logger.info("修改商品成功，已清除相关缓存，商品ID: {}", goods.getId());
				return "redirect:/goods/selectAllGoodsByPage?currentPage=1&act=updateSelect";
			}
			// 失败
			return "admin/UpdateAGoods";
		}
	}

	@Override
	public String toAddGoods(Goods goods, Model model) {
		goods.setStatus(1); // 默认上架
		model.addAttribute("goodsType", goodsRepository.selectAllGoodsType());
		return "admin/addGoods";
	}

	@Override
	public String detail(Model model, Integer id, String act) {
		model.addAttribute("goods", goodsRepository.selectAGoods(id));
		if ("detail".equals(act))
			return "admin/detail";
		else {
			model.addAttribute("goodsType", goodsRepository.selectAllGoodsType());
			return "admin/updateAGoods";
		}
	}

	@Override
	public String delete(Integer id) {
		if (goodsRepository.selectCartGoods(id).size() > 0
				|| goodsRepository.selectOrderGoods(id).size() > 0)
			return "no";
		else {
			// 获取商品信息用于清除缓存
			Goods goods = goodsRepository.selectAGoods(id);
			goodsRepository.deleteAGoods(id);

			// 清除相关缓存
			if (goods != null) {
				clearGoodsRelatedCache(id, goods.getGoodstype_id());
				logger.info("删除商品成功，已清除相关缓存，商品ID: {}", id);
			}
			return "/goods/selectAllGoodsByPage?currentPage=1&act=deleteSelect";
		}
	}

	/**
	 * 清除商品相关的所有缓存
	 * 
	 * @param goodsId 商品ID
	 * @param typeId  商品类型ID
	 */
	private void clearGoodsRelatedCache(Integer goodsId, Integer typeId) {
		try {
			// 清除商品详情缓存
			cacheService.evict("goods:detail:" + goodsId);

			// 清除该类型的最新商品列表缓存
			cacheService.evict("index:lasted_goods:tid_" + typeId);

			// 清除所有类型的最新商品列表缓存（包括tid=0的全部商品）
			cacheService.evictByPattern("index:lasted_goods:tid_*");

			// 清除商品类型列表缓存（可能有商品数量变化）
			cacheService.evict("index:goods_types");

			// 清除所有搜索结果缓存
			cacheService.evictByPattern("index:search:*");

			logger.debug("已清除商品相关缓存，商品ID: {}, 类型ID: {}", goodsId, typeId);
		} catch (Exception e) {
			logger.error("清除商品缓存时发生错误", e);
		}
	}

}
