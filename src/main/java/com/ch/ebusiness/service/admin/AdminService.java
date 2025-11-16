package com.ch.ebusiness.service.admin;

import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

import com.ch.ebusiness.entity.AUser;

import java.util.List;

public interface AdminService {
	public String login(AUser aUser, HttpSession session, Model model);

	/**
	 * 查询所有管理员（分页）
	 */
	List<AUser> getAllAdmins(int pageNum, int pageSize);

	/**
	 * 统计管理员总数
	 */
	int countAllAdmins();

	/**
	 * 添加管理员
	 */
	int addAdmin(AUser admin);

	/**
	 * 更新管理员信息
	 */
	int updateAdmin(AUser admin);

	/**
	 * 删除管理员
	 */
	int deleteAdmin(Integer id);
}
