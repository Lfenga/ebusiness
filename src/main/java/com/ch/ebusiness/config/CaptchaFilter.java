package com.ch.ebusiness.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 验证码过滤器
 * 在 Spring Security 认证之前，先校验验证码
 */
public class CaptchaFilter extends OncePerRequestFilter {

    private String loginProcessingUrl = "/user/securityLogin";
    private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler(
            "/user/toLogin?error=captcha");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 只在登录请求时校验验证码
        if (loginProcessingUrl.equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod())) {
            try {
                validateCaptcha(request);
            } catch (AuthenticationException e) {
                // 验证码错误，不再继续认证流程
                failureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }

        // 验证码校验通过，继续后续过滤器
        filterChain.doFilter(request, response);
    }

    /**
     * 校验验证码
     */
    private void validateCaptcha(HttpServletRequest request) throws AuthenticationException {
        HttpSession session = request.getSession(false);
        String sessionCaptcha = (session != null) ? (String) session.getAttribute("rand") : null;
        String inputCaptcha = request.getParameter("code");

        if (sessionCaptcha == null || inputCaptcha == null || !sessionCaptcha.equalsIgnoreCase(inputCaptcha)) {
            throw new AuthenticationServiceException("验证码错误");
        }

        // 验证码一次性使用，验证后清除
        if (session != null) {
            session.removeAttribute("rand");
        }
    }
}
