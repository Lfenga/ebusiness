package com.ch.ebusiness.service.admin;

import com.ch.ebusiness.entity.AdminOperationLog;
import com.ch.ebusiness.entity.AUser;
import com.ch.ebusiness.repository.admin.AdminLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 管理员操作日志服务实现
 */
@Service
public class AdminLogServiceImpl implements AdminLogService {

	@Autowired
	private AdminLogRepository adminLogRepository;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void saveLog(AUser admin, String module, String operationType, String description) {
		saveDetailLog(admin, module, operationType, description, null, null,
				null, null, null, null, 0L, "SUCCESS", null);
	}

	@Override
	public void saveDetailLog(AUser admin, String module, String operationType, String description,
			String targetType, String targetId, String requestMethod,
			String requestParams, String ipAddress, String browserInfo,
			long executionTime, String status, String errorMsg) {
		AdminOperationLog log = new AdminOperationLog();
		log.setAdminId(admin.getId());
		log.setAdminName(admin.getAname());
		log.setAdminRealname(admin.getArealname() != null ? admin.getArealname() : admin.getAname());
		log.setOperationType(operationType);
		log.setModule(module);
		log.setOperationDesc(description);
		log.setTargetType(targetType);
		log.setTargetId(targetId);
		log.setRequestMethod(requestMethod);
		log.setRequestParams(requestParams);
		log.setIpAddress(ipAddress);
		log.setBrowserInfo(browserInfo);
		log.setOperationTime(new Date());
		log.setExecutionTime(executionTime);
		log.setStatus(status);
		log.setErrorMsg(errorMsg);

		adminLogRepository.insertLog(log);
	}

	@Override
	public Object recordLog(ProceedingJoinPoint joinPoint, AUser admin, String module,
			String operationType, String description, String targetType,
			String ipAddress, String browserInfo) throws Throwable {
		long startTime = System.currentTimeMillis();
		String status = "SUCCESS";
		String errorMsg = null;
		Object result = null;
		String targetId = null;

		// 尝试从方法参数中提取目标ID和描述信息
		Object[] args = joinPoint.getArgs();
		String enhancedDescription = description;
		targetId = extractTargetIdFromArgs(args, targetType);
		if (targetId != null) {
			enhancedDescription = enrichDescription(description, args, targetType);
		}

		try {
			// 执行目标方法
			result = joinPoint.proceed();

			// 对于ADD操作,执行后再次尝试从参数中提取ID(MyBatis会回填生成的ID)
			if (targetId == null && operationType.equals("ADD")) {
				targetId = extractTargetIdFromArgs(args, targetType);
			}

			// 如果参数中没有ID,尝试从返回结果中提取
			if (targetId == null && result != null && (operationType.equals("ADD") || operationType.equals("UPDATE"))) {
				targetId = extractTargetId(result);
			}

			return result;
		} catch (Exception e) {
			status = "FAIL";
			errorMsg = e.getMessage();
			if (errorMsg != null && errorMsg.length() > 500) {
				errorMsg = errorMsg.substring(0, 500);
			}
			throw e;
		} finally {
			long executionTime = System.currentTimeMillis() - startTime;

			// 获取方法名和参数
			String methodName = joinPoint.getSignature().getName();
			String requestParams = null;
			try {
				requestParams = serializeBusinessParams(args);
				if (requestParams.length() > 2000) {
					requestParams = requestParams.substring(0, 2000);
				}
			} catch (Exception e) {
				requestParams = "参数序列化失败: " + e.getMessage();
			}

			// 保存日志
			saveDetailLog(admin, module, operationType, enhancedDescription,
					targetType, targetId, methodName, requestParams,
					ipAddress, browserInfo, executionTime, status, errorMsg);
		}
	}

	private String extractTargetId(Object result) {
		if (result instanceof Integer) {
			return String.valueOf(result);
		} else if (result instanceof String) {
			return (String) result;
		}
		// 可以扩展更多类型的提取逻辑
		return null;
	}

	/**
	 * 智能序列化业务参数,过滤不可序列化对象
	 */
	private String serializeBusinessParams(Object[] args) {
		if (args == null || args.length == 0) {
			return "[]";
		}

		StringBuilder sb = new StringBuilder("[");
		boolean first = true;

		for (Object arg : args) {
			if (arg == null) {
				continue;
			}

			// 跳过不可序列化的对象
			String className = arg.getClass().getName();
			if (className.contains("HttpServletRequest")
					|| className.contains("HttpServletResponse")
					|| className.contains("MultipartFile")
					|| className.contains("Model")
					|| className.contains("HttpSession")) {
				continue;
			}

			// 处理基本类型和业务对象
			if (!first) {
				sb.append(", ");
			}

			try {
				if (arg instanceof String || arg instanceof Number || arg instanceof Boolean) {
					sb.append(objectMapper.writeValueAsString(arg));
				} else {
					// 业务对象
					sb.append(objectMapper.writeValueAsString(arg));
				}
				first = false;
			} catch (Exception e) {
				// 如果序列化失败,记录类名
				sb.append("\"").append(arg.getClass().getSimpleName()).append(" 对象\"");
				first = false;
			}
		}

		sb.append("]");
		return sb.toString();
	}

	/**
	 * 从方法参数中提取目标对象ID
	 */
	private String extractTargetIdFromArgs(Object[] args, String targetType) {
		if (args == null || targetType == null || targetType.isEmpty()) {
			return null;
		}

		for (Object arg : args) {
			if (arg == null) {
				continue;
			}

			// 处理直接传入的ID参数(如 delete(int id))
			if (arg instanceof Integer) {
				int id = (Integer) arg;
				if (id > 0) {
					return String.valueOf(id);
				}
			}

			try {
				// 根据targetType匹配对应的业务对象
				if ("Goods".equals(targetType) && arg.getClass().getSimpleName().equals("Goods")) {
					java.lang.reflect.Method getIdMethod = arg.getClass().getMethod("getId");
					Object id = getIdMethod.invoke(arg);
					if (id != null && (Integer) id > 0) {
						return String.valueOf(id);
					}
				} else if ("AUser".equals(targetType) && arg.getClass().getSimpleName().equals("AUser")) {
					java.lang.reflect.Method getIdMethod = arg.getClass().getMethod("getId");
					Object id = getIdMethod.invoke(arg);
					if (id != null && (Integer) id > 0) {
						return String.valueOf(id);
					}
				} else if ("GoodsType".equals(targetType) && arg.getClass().getSimpleName().equals("GoodsType")) {
					java.lang.reflect.Method getIdMethod = arg.getClass().getMethod("getId");
					Object id = getIdMethod.invoke(arg);
					if (id != null && (Integer) id > 0) {
						return String.valueOf(id);
					}
				}
			} catch (Exception e) {
				// 忽略反射异常
			}
		}
		return null;
	}

	/**
	 * 丰富描述信息,添加业务对象的关键信息
	 */
	private String enrichDescription(String description, Object[] args, String targetType) {
		if (args == null || targetType == null || targetType.isEmpty()) {
			return description;
		}

		try {
			for (Object arg : args) {
				if (arg == null) {
					continue;
				}

				if ("Goods".equals(targetType) && arg.getClass().getSimpleName().equals("Goods")) {
					java.lang.reflect.Method getIdMethod = arg.getClass().getMethod("getId");
					java.lang.reflect.Method getNameMethod = arg.getClass().getMethod("getGname");
					java.lang.reflect.Method getStatusMethod = arg.getClass().getMethod("getStatus");

					Object id = getIdMethod.invoke(arg);
					Object name = getNameMethod.invoke(arg);
					Object status = getStatusMethod.invoke(arg);

					if (id != null && (Integer) id > 0) {
						String statusText = (status != null && (Integer) status == 1) ? "上架" : "下架";
						return description + " [ID:" + id + ", 名称:" + name + ", 状态:" + statusText + "]";
					} else if (name != null) {
						return description + " [名称:" + name + "]";
					}
				} else if ("AUser".equals(targetType) && arg.getClass().getSimpleName().equals("AUser")) {
					java.lang.reflect.Method getNameMethod = arg.getClass().getMethod("getAname");
					Object name = getNameMethod.invoke(arg);
					if (name != null) {
						return description + " [用户名:" + name + "]";
					}
				} else if ("GoodsType".equals(targetType) && arg.getClass().getSimpleName().equals("GoodsType")) {
					java.lang.reflect.Method getNameMethod = arg.getClass().getMethod("getTypename");
					Object name = getNameMethod.invoke(arg);
					if (name != null) {
						return description + " [类型名:" + name + "]";
					}
				}
			}
		} catch (Exception e) {
			// 如果丰富描述失败,返回原描述
		}

		return description;
	}

	@Override
	public List<AdminOperationLog> getAllLogs(int pageNum, int pageSize) {
		int offset = (pageNum - 1) * pageSize;
		return adminLogRepository.selectAllLogs(offset, pageSize);
	}

	@Override
	public int countAllLogs() {
		return adminLogRepository.countAllLogs();
	}

	@Override
	public List<AdminOperationLog> queryLogsByConditions(Integer adminId, String module,
			String operationType, String status,
			Date startTime, Date endTime,
			int pageNum, int pageSize) {
		int offset = (pageNum - 1) * pageSize;
		return adminLogRepository.selectLogsByConditions(adminId, module, operationType,
				status, startTime, endTime, offset, pageSize);
	}

	@Override
	public int countLogsByConditions(Integer adminId, String module, String operationType,
			String status, Date startTime, Date endTime) {
		return adminLogRepository.countLogsByConditions(adminId, module, operationType,
				status, startTime, endTime);
	}
}
