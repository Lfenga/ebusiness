package com.ch.ebusiness.service.admin;

import com.ch.ebusiness.entity.AdminOperationLog;
import com.ch.ebusiness.entity.AUser;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 管理员操作日志服务接口
 */
public interface AdminLogService {

	/**
	 * 保存简单操作日志
	 */
	void saveLog(AUser admin, String module, String operationType, String description);

	/**
	 * 保存详细操作日志
	 */
	void saveDetailLog(AUser admin, String module, String operationType, String description,
			String targetType, String targetId, String requestMethod,
			String requestParams, String ipAddress, String browserInfo,
			long executionTime, String status, String errorMsg);

	/**
	 * AOP自动记录日志（由切面调用）
	 * 
	 * @return 返回目标方法的执行结果
	 */
	Object recordLog(ProceedingJoinPoint joinPoint, AUser admin, String module,
			String operationType, String description, String targetType,
			String ipAddress, String browserInfo) throws Throwable;

	/**
	 * 查询所有日志（分页）
	 */
	List<AdminOperationLog> getAllLogs(int pageNum, int pageSize);

	/**
	 * 统计总日志数
	 */
	int countAllLogs();

	/**
	 * 多条件查询日志
	 */
	List<AdminOperationLog> queryLogsByConditions(Integer adminId, String module,
			String operationType, String status,
			Date startTime, Date endTime,
			int pageNum, int pageSize);

	/**
	 * 统计符合条件的日志数
	 */
	int countLogsByConditions(Integer adminId, String module, String operationType,
			String status, Date startTime, Date endTime);
}
