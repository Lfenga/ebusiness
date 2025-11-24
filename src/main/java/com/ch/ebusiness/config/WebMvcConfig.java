package com.ch.ebusiness.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置类
 * 配置静态资源映射，使上传的图片能够实时访问而无需重启
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 配置静态资源处理器
     * 将/images/** 映射到文件系统的实际路径，实现图片热加载
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取项目根目录
        String projectPath = System.getProperty("user.dir");

        // 开发环境：映射到项目源码目录
        String devImagesPath = "file:" + projectPath + "/src/main/resources/static/images/";

        // 生产环境：映射到编译后的target目录
        String prodImagesPath = "file:" + projectPath + "/target/classes/static/images/";

        registry.addResourceHandler("/images/**")
                .addResourceLocations(devImagesPath, prodImagesPath)
                .setCachePeriod(0); // 禁用缓存，确保图片立即生效

        // 保留默认的静态资源映射
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600); // 其他静态资源缓存1小时
    }
}
