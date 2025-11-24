package com.ch.ebusiness.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ch.ebusiness.util.MoneyUtil;

/**
 * Thymeleaf配置类
 * 注册工具类供模板使用
 */
@Configuration
public class ThymeleafConfig {

    /**
     * 注册MoneyUtil工具类到Spring容器
     * 在Thymeleaf模板中可以通过 ${@moneyUtil.format(...)} 使用
     */
    @Bean(name = "moneyUtil")
    public MoneyUtil moneyUtil() {
        return new MoneyUtil();
    }
}
