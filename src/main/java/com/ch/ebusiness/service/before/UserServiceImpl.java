package com.ch.ebusiness.service.before;

import java.util.List;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import com.ch.ebusiness.entity.BUser;
import com.ch.ebusiness.repository.before.UserRepository;
import com.ch.ebusiness.util.MD5Util;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;

	@Override
	public String isUse(BUser bUser) {
		if (userRepository.isUse(bUser).size() > 0) {
			return "no";
		}
		return "ok";
	}

	@Override
	public String register(BUser bUser) {
		// 新用户密码用BCrypt加密
		bUser.setBpwd(passwordEncoder.encode(bUser.getBpwd()));
		if (userRepository.register(bUser) > 0) {
			return "user/login";
		}
		return "user/register";
	}

	@Override
	public String login(BUser bUser, HttpSession session, Model model) {
		String rand = (String) session.getAttribute("rand");
		if (rand == null || bUser.getCode() == null || !rand.equalsIgnoreCase(bUser.getCode())) {
			model.addAttribute("errorMessage", "验证码错误！");
			return "user/login";
		}
		// 先查用户
		BUser user = userRepository.selectByEmail(bUser.getBemail());
		if (user == null) {
			model.addAttribute("errorMessage", "用户名或密码错误！");
			return "user/login";
		}
		String stored = user.getBpwd();
		String raw = bUser.getBpwd();
		if (stored != null && stored.startsWith("$2")) {
			// BCrypt验证
			if (passwordEncoder.matches(raw, stored)) {
				session.setAttribute("bUser", user);
				return "redirect:/";
			}
		} else {
			// MD5兼容验证
			if (stored != null && stored.equals(MD5Util.MD5(raw))) {
				// 将MD5密码迁移为BCrypt
				String encoded = passwordEncoder.encode(raw);
				userRepository.updatePasswordByEmail(user.getBemail(), encoded);
				user.setBpwd(encoded);
				session.setAttribute("bUser", user);
				return "redirect:/";
			}
		}
		model.addAttribute("errorMessage", "用户名或密码错误！");
		return "user/login";
	}

}
