package com.ch.ebusiness.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 管理员操作日志注解
 * 用于标记需要记录操作日志的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminLog {

	/**
	 * 操作模块
	 */
	String module();

	/**
	 * 操作类型：ADD, UPDATE, DELETE, QUERY
	 */
	String operationType();

	/**
	 * 操作描述
	 */
	String description();

	/**
	 * 目标类型（可选）
	 */
	String targetType() default "";
}
