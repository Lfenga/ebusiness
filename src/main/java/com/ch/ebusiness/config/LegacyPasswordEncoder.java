package com.ch.ebusiness.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ch.ebusiness.util.MD5Util;

/**
 * 兼容 MD5 和 BCrypt 的密码编码器
 * 用于渐进式迁移：老用户用MD5，新用户用BCrypt
 */
public class LegacyPasswordEncoder implements PasswordEncoder {

    private BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        // 新密码统一用 BCrypt 加密
        return bcryptEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // 如果是 BCrypt 格式（$2a$/$2b$开头）
        if (encodedPassword != null && encodedPassword.startsWith("$2")) {
            return bcryptEncoder.matches(rawPassword, encodedPassword);
        }
        // 否则当作 MD5 处理（兼容老数据）
        String md5 = MD5Util.MD5(rawPassword.toString());
        return md5.equals(encodedPassword);
    }
}
