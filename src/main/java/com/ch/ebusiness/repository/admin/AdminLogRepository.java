package com.ch.ebusiness.repository.admin;

import com.ch.ebusiness.entity.AdminOperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 管理员操作日志数据访问接口
 */
@Mapper
@Repository
public interface AdminLogRepository {

	/**
	 * 插入操作日志
	 */
	int insertLog(AdminOperationLog log);

	/**
	 * 查询所有日志（分页）
	 */
	List<AdminOperationLog> selectAllLogs(@Param("offset") int offset, @Param("limit") int limit);

	/**
	 * 统计总日志数
	 */
	int countAllLogs();

	/**
	 * 根据管理员ID查询日志
	 */
	List<AdminOperationLog> selectLogsByAdminId(@Param("adminId") Integer adminId,
			@Param("offset") int offset,
			@Param("limit") int limit);

	/**
	 * 根据模块查询日志
	 */
	List<AdminOperationLog> selectLogsByModule(@Param("module") String module,
			@Param("offset") int offset,
			@Param("limit") int limit);

	/**
	 * 根据操作类型查询日志
	 */
	List<AdminOperationLog> selectLogsByOperationType(@Param("operationType") String operationType,
			@Param("offset") int offset,
			@Param("limit") int limit);

	/**
	 * 根据时间范围查询日志
	 */
	List<AdminOperationLog> selectLogsByTimeRange(@Param("startTime") Date startTime,
			@Param("endTime") Date endTime,
			@Param("offset") int offset,
			@Param("limit") int limit);

	/**
	 * 多条件组合查询日志
	 */
	List<AdminOperationLog> selectLogsByConditions(@Param("adminId") Integer adminId,
			@Param("module") String module,
			@Param("operationType") String operationType,
			@Param("status") String status,
			@Param("startTime") Date startTime,
			@Param("endTime") Date endTime,
			@Param("offset") int offset,
			@Param("limit") int limit);

	/**
	 * 统计符合条件的日志数
	 */
	int countLogsByConditions(@Param("adminId") Integer adminId,
			@Param("module") String module,
			@Param("operationType") String operationType,
			@Param("status") String status,
			@Param("startTime") Date startTime,
			@Param("endTime") Date endTime);
}
