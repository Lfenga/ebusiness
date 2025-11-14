package com.ch.ebusiness.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ch.ebusiness.entity.BUser;
import com.ch.ebusiness.repository.before.UserRepository;

/**
 * Spring Security 用户认证服务
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username 这里是邮箱
        BUser user = userRepository.selectByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在：" + username);
        }

        // 返回 Spring Security 的 User 对象
        // 注意：这里的密码应该是数据库中存储的加密密码
        return User.builder()
                .username(user.getBemail())
                .password(user.getBpwd()) // 数据库存储的密码（BCrypt或MD5）
                .authorities(AuthorityUtils.createAuthorityList("ROLE_USER"))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
