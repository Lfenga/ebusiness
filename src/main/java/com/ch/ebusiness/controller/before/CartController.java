package com.ch.ebusiness.controller.before;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ch.ebusiness.entity.Goods;
import com.ch.ebusiness.entity.Order;
import com.ch.ebusiness.service.before.CartService;

@Controller
@RequestMapping("/cart")
public class CartController extends BeforeBaseController {
	@Autowired
	private CartService cartService;

	@RequestMapping("/putCart")
	public String putCart(Goods goods, Model model, HttpSession session) {
		return cartService.putCart(goods, model, session);
	}

	@RequestMapping("/selectCart")
	public String selectCart(Model model, HttpSession session, String act) {
		return cartService.selectCart(model, session, act);
	}

	@RequestMapping("/deleteCart")
	public String deleteCart(HttpSession session, Integer gid) {
		return cartService.deleteCart(session, gid);
	}

	@RequestMapping("/clearCart")
	public String clearCart(HttpSession session) {
		return cartService.clearCart(session);
	}

	@RequestMapping("/submitOrder")
	public String submitOrder(Order order, Model model, HttpSession session) {
		return cartService.submitOrder(order, model, session);
	}

	@RequestMapping("/pay")
	@ResponseBody
	public String pay(@RequestBody Order order) {
		return cartService.pay(order);
	}

	@RequestMapping("/myOder")
	public String myOder(Model model, HttpSession session) {
		return cartService.myOder(model, session);
	}

	@RequestMapping("/orderDetail")
	public String orderDetail(Model model, Integer id) {
		return cartService.orderDetail(model, id);
	}

	@RequestMapping("/userInfo")
	public String userInfo() {
		return "user/userInfo";
	}

	@RequestMapping("/updatePassword")
	public String updatePassword(HttpSession session, Model model, String newPassword,
			String confirmPassword, String captcha) {
		// 从 Spring Security 上下文获取当前登录用户
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()
				|| "anonymousUser".equals(authentication.getPrincipal())) {
			model.addAttribute("msg", "请先登录");
			return "redirect:/user/toLogin";
		}
		String currentUserEmail = authentication.getName(); // 当前登录用户的邮箱

		// 参数校验
		if (newPassword == null || newPassword.length() < 6) {
			model.addAttribute("msg", "新密码至少6位");
			return "user/userInfo";
		}
		if (!newPassword.equals(confirmPassword)) {
			model.addAttribute("msg", "两次输入的新密码不一致");
			return "user/userInfo";
		}
		String sessionCode = (String) session.getAttribute("rand");
		if (captcha == null || sessionCode == null || !captcha.equalsIgnoreCase(sessionCode)) {
			model.addAttribute("msg", "验证码错误");
			return "user/userInfo";
		}

		// 验证码使用后清除
		session.removeAttribute("rand");

		// 调用 Service 修改密码（使用当前登录用户的邮箱）
		String result = cartService.updatePasswordSecure(currentUserEmail, newPassword);
		if (result != null) {
			model.addAttribute("msg", result);
			return "user/userInfo";
		}

		// 密码修改成功，登出并重定向到登录页
		SecurityContextHolder.clearContext(); // 清除安全上下文
		session.invalidate(); // 清除session
		return "redirect:/user/toLogin?passwordChanged=true";
	}
}
