package com.ch.ebusiness.service.admin;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.ch.ebusiness.entity.AUser;
import com.ch.ebusiness.repository.admin.AdminRepository;
import com.ch.ebusiness.util.MD5Util;

@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public String login(AUser aUser, HttpSession session, Model model) {
		// 根据用户名查询管理员
		AUser dbAdmin = adminRepository.selectByAname(aUser.getAname());

		if (dbAdmin == null) {
			model.addAttribute("errorMessage", "用户名或密码错误！");
			return "admin/login";
		}

		// 验证密码（支持MD5和BCrypt）
		boolean passwordMatches = false;
		String inputPassword = aUser.getApwd();
		String dbPassword = dbAdmin.getApwd();

		// 判断数据库密码是BCrypt还是MD5
		if (dbPassword.startsWith("$2a$") || dbPassword.startsWith("$2b$")) {
			// BCrypt密码
			passwordMatches = passwordEncoder.matches(inputPassword, dbPassword);
		} else {
			// MD5密码，需要先MD5加密输入的密码再比较
			String md5Password = MD5Util.string2MD5(inputPassword);
			passwordMatches = md5Password.equals(dbPassword);

			// 如果MD5密码验证成功，自动升级为BCrypt
			if (passwordMatches) {
				String bcryptPassword = passwordEncoder.encode(inputPassword);
				dbAdmin.setApwd(bcryptPassword);
				adminRepository.updateAdmin(dbAdmin);
			}
		}

		if (passwordMatches) {
			// 登录成功，更新登录时间
			adminRepository.updateLastLoginTime(dbAdmin.getId());
			// 重新查询完整信息
			dbAdmin = adminRepository.selectByAname(aUser.getAname());
			session.setAttribute("aUser", dbAdmin);
			return "redirect:/goods/selectAllGoodsByPage?currentPage=1&act=select";
		} else {
			model.addAttribute("errorMessage", "用户名或密码错误！");
			return "admin/login";
		}
	}

	@Override
	public List<AUser> getAllAdmins(int pageNum, int pageSize) {
		int offset = (pageNum - 1) * pageSize;
		return adminRepository.selectAllAdmins(offset, pageSize);
	}

	@Override
	public int countAllAdmins() {
		return adminRepository.countAllAdmins();
	}

	@Override
	public int addAdmin(AUser admin) {
		// 密码使用BCrypt加密
		String encryptedPassword = passwordEncoder.encode(admin.getApwd());
		admin.setApwd(encryptedPassword);
		admin.setAstatus(1); // 默认启用
		return adminRepository.insertAdmin(admin);
	}

	@Override
	public int updateAdmin(AUser admin) {
		return adminRepository.updateAdmin(admin);
	}

	@Override
	public int deleteAdmin(Integer id) {
		return adminRepository.deleteAdmin(id);
	}
}
