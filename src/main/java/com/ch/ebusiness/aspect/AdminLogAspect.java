package com.ch.ebusiness.aspect;

import com.ch.ebusiness.annotation.AdminLog;
import com.ch.ebusiness.entity.AUser;
import com.ch.ebusiness.service.admin.AdminLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

/**
 * 管理员操作日志AOP切面
 * 拦截所有带有@AdminLog注解的方法，自动记录操作日志
 */
@Aspect
@Component
public class AdminLogAspect {

	@Autowired
	private AdminLogService adminLogService;

	/**
	 * 环绕通知：拦截带有@AdminLog注解的方法
	 */
	@Around("@annotation(com.ch.ebusiness.annotation.AdminLog)")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		// 获取当前请求
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes == null) {
			return joinPoint.proceed();
		}

		HttpServletRequest request = attributes.getRequest();
		HttpSession session = request.getSession(false);

		// 获取当前管理员信息
		if (session == null) {
			return joinPoint.proceed();
		}

		AUser admin = (AUser) session.getAttribute("aUser");
		if (admin == null) {
			return joinPoint.proceed();
		}

		// 获取@AdminLog注解信息
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		AdminLog adminLog = method.getAnnotation(AdminLog.class);

		if (adminLog == null) {
			return joinPoint.proceed();
		}

		// 获取请求信息
		String ipAddress = getIpAddress(request);
		String browserInfo = getBrowserInfo(request);

		// 调用日志服务记录日志，并返回目标方法的执行结果
		return adminLogService.recordLog(joinPoint, admin, adminLog.module(),
				adminLog.operationType(), adminLog.description(),
				adminLog.targetType(), ipAddress, browserInfo);
	}

	/**
	 * 获取客户端IP地址
	 */
	private String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		// 处理多级代理的情况
		if (ip != null && ip.contains(",")) {
			ip = ip.split(",")[0];
		}
		return ip;
	}

	/**
	 * 获取浏览器信息
	 */
	private String getBrowserInfo(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		if (userAgent == null) {
			return "Unknown";
		}
		// 简化浏览器信息提取
		if (userAgent.contains("Chrome")) {
			return "Chrome";
		} else if (userAgent.contains("Firefox")) {
			return "Firefox";
		} else if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) {
			return "Safari";
		} else if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
			return "IE";
		} else if (userAgent.contains("Edge")) {
			return "Edge";
		}
		return "Other";
	}
}
