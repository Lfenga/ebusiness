package com.ch.ebusiness.controller.admin;

import com.ch.ebusiness.annotation.AdminLog;
import com.ch.ebusiness.entity.AdminOperationLog;
import com.ch.ebusiness.service.admin.AdminLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

/**
 * 管理员日志查询控制器
 */
@Controller
@RequestMapping("/adminLog")
public class AdminLogController extends AdminBaseController {

	@Autowired
	private AdminLogService adminLogService;

	/**
	 * 查询管理员操作日志（分页+条件查询）
	 */
	@RequestMapping("/selectLogs")
	public String selectLogs(Model model,
			@RequestParam(defaultValue = "1") int currentPage,
			@RequestParam(required = false) Integer adminId,
			@RequestParam(required = false) String module,
			@RequestParam(required = false) String operationType,
			@RequestParam(required = false) String status,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime) {

		int pageSize = 20;

		// 查询日志列表
		List<AdminOperationLog> logs = adminLogService.queryLogsByConditions(
				adminId, module, operationType, status, startTime, endTime, currentPage, pageSize);

		// 统计总记录数
		int totalCount = adminLogService.countLogsByConditions(
				adminId, module, operationType, status, startTime, endTime);

		int totalPage = (totalCount + pageSize - 1) / pageSize;

		// 设置模型数据
		model.addAttribute("logs", logs);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("totalCount", totalCount);

		// 保持查询条件
		model.addAttribute("adminId", adminId);
		model.addAttribute("module", module);
		model.addAttribute("operationType", operationType);
		model.addAttribute("status", status);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);

		return "admin/selectAdminLogs";
	}
}
