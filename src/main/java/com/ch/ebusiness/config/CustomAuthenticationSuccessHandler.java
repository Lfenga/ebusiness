package com.ch.ebusiness.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ch.ebusiness.entity.BUser;
import com.ch.ebusiness.repository.before.UserRepository;

/**
 * 自定义登录成功处理器
 * 登录成功后，从数据库查询用户完整信息并存入 session
 * 以便页面可以继续使用 ${session.bUser}
 */
@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    public CustomAuthenticationSuccessHandler() {
        setDefaultTargetUrl("/"); // 默认跳转首页
        setAlwaysUseDefaultTargetUrl(false); // 支持跳转到登录前访问的页面
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {

        // 获取登录用户的邮箱（username）
        String email = authentication.getName();

        // 从数据库查询用户完整信息
        BUser user = userRepository.selectByEmail(email);

        if (user != null) {
            // 将用户信息存入 session（兼容原有页面逻辑）
            HttpSession session = request.getSession();
            session.setAttribute("bUser", user);
        }

        // 继续原有的跳转逻辑
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
