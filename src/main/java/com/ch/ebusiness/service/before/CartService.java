package com.ch.ebusiness.service.before;

import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

import com.ch.ebusiness.entity.Goods;
import com.ch.ebusiness.entity.Order;

public interface CartService {
	public String putCart(Goods goods, Model model, HttpSession session);

	public String selectCart(Model model, HttpSession session, String act);

	public String deleteCart(HttpSession session, Integer gid);

	public String clearCart(HttpSession session);

	public String submitOrder(Order order, Model model, HttpSession session);

	public String pay(Order order);

	public String myOder(Model model, HttpSession session);

	public String myOder(Model model, HttpSession session, Integer page);

	public String orderDetail(Model model, Integer id);

	public String updateUpwd(HttpSession session, String bpwd);

	/**
	 * 修改密码（Spring Security加密）- 安全版本
	 * 从 Spring Security 上下文获取当前用户，只允许修改自己的密码
	 * 
	 * @param currentUserEmail 当前登录用户邮箱（从SecurityContext获取）
	 * @param newPassword      新密码
	 * @return 错误信息（成功返回null）
	 */
	public String updatePasswordSecure(String currentUserEmail, String newPassword);

	/**
	 * 修改密码（Spring Security加密）- 旧版本（保留兼容）
	 * 
	 * @param session     用户session
	 * @param bemail      邮箱
	 * @param newPassword 新密码
	 * @return 错误信息（成功返回null）
	 */
	@Deprecated
	public String updatePassword(HttpSession session, String bemail, String newPassword);
}
