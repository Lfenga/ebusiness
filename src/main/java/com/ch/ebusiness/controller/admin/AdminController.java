package com.ch.ebusiness.controller.admin;

import javax.servlet.http.HttpSession;

import com.ch.ebusiness.annotation.AdminLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ch.ebusiness.entity.AUser;
import com.ch.ebusiness.service.admin.AdminService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private AdminService adminService;

	/**
	 * 跳转到登录页(不需要登录验证)
	 */
	@RequestMapping("/toLogin")
	public String toLogin(Model model) {
		model.addAttribute("aUser", new AUser());
		return "admin/login";
	}

	/**
	 * 管理员登录
	 */
	@RequestMapping("/login")
	public String login(AUser aUser, HttpSession session, Model model) {
		return adminService.login(aUser, session, model);
	}

	/**
	 * 管理员个人信息页面
	 */
	@RequestMapping("/info")
	public String adminInfo(HttpSession session, Model model) {
		AUser admin = (AUser) session.getAttribute("aUser");
		if (admin == null) {
			return "redirect:/admin/toLogin";
		}
		model.addAttribute("admin", admin);
		return "admin/adminInfo";
	}

	/**
	 * 查询所有管理员（分页）
	 */
	@RequestMapping("/selectAllAdmins")
	public String selectAllAdmins(Model model, @RequestParam(defaultValue = "1") int currentPage) {
		int pageSize = 10;
		List<AUser> admins = adminService.getAllAdmins(currentPage, pageSize);
		int totalCount = adminService.countAllAdmins();
		int totalPage = (totalCount + pageSize - 1) / pageSize;

		model.addAttribute("admins", admins);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("totalCount", totalCount);

		return "admin/selectAdmins";
	}

	/**
	 * 跳转到添加管理员页面
	 */
	@RequestMapping("/toAddAdmin")
	public String toAddAdmin(@ModelAttribute("admin") AUser admin) {
		return "admin/addAdmin";
	}

	/**
	 * 添加管理员
	 */
	@RequestMapping("/addAdmin")
	@AdminLog(module = "管理员管理", operationType = "ADD", description = "添加管理员", targetType = "AUser")
	public String addAdmin(@ModelAttribute("admin") AUser admin, Model model) {
		try {
			adminService.addAdmin(admin);
			return "redirect:/admin/selectAllAdmins?currentPage=1";
		} catch (Exception e) {
			model.addAttribute("errorMessage", "添加失败：" + e.getMessage());
			return "admin/addAdmin";
		}
	}

	/**
	 * 删除管理员
	 */
	@RequestMapping("/deleteAdmin")
	@ResponseBody
	@AdminLog(module = "管理员管理", operationType = "DELETE", description = "删除管理员", targetType = "AUser")
	public String deleteAdmin(@RequestParam Integer id) {
		try {
			adminService.deleteAdmin(id);
			return "yes";
		} catch (Exception e) {
			return "no";
		}
	}
}
