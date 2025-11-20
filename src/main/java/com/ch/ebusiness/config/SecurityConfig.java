package com.ch.ebusiness.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 安全配置
 * 1. 配置用户认证（UserDetailsService + PasswordEncoder）
 * 2. 配置访问控制（哪些URL需要登录，哪些可以匿名访问）
 * 3. 配置表单登录、登出
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    /**
     * BCrypt 密码编码器（用于新用户注册和密码修改）
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 兼容 MD5 和 BCrypt 的密码编码器（用于登录认证）
     */
    @Bean
    public PasswordEncoder legacyPasswordEncoder() {
        return new LegacyPasswordEncoder();
    }

    /**
     * 配置用户认证
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(legacyPasswordEncoder()); // 使用兼容编码器
    }

    /**
     * 配置 HTTP 安全策略
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // 允许匿名访问的URL
                .antMatchers("/", "/index", "/toIndex", "/selectAGoods").permitAll()
                .antMatchers("/user/toLogin", "/user/login", "/user/toRegister", "/user/register", "/user/isUse")
                .permitAll()
                .antMatchers("/validateCode").permitAll()
                .antMatchers("/css/**", "/js/**", "/fonts/**", "/images/**", "/imgs/**").permitAll()
                // 需要登录才能访问的URL（用户相关功能）
                .antMatchers("/cart/**", "/user/exit").authenticated()
                // 管理员URL不走Spring Security，使用session验证
                .antMatchers("/admin/**", "/goods/**", "/type/**", "/selectOrder", "/selectUser", "/deleteUser",
                        "/loginOut", "/adminLog/**", "/statistics/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/user/toLogin") // 自定义登录页
                .loginProcessingUrl("/user/securityLogin") // 登录表单提交URL
                .usernameParameter("bemail") // 表单用户名字段（这里是邮箱）
                .passwordParameter("bpwd") // 表单密码字段
                .successHandler(customAuthenticationSuccessHandler) // 使用自定义登录成功处理器
                .failureUrl("/user/toLogin?error=true") // 登录失败跳转
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/user/logout") // 登出URL
                .logoutSuccessUrl("/user/toLogin?logout=true") // 登出成功跳转
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and()
                .addFilterBefore(new CaptchaFilter(), UsernamePasswordAuthenticationFilter.class) // 添加验证码过滤器
                .csrf().disable(); // 暂时禁用CSRF（生产环境建议启用）
    }
}
