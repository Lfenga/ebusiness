package com.ch.ebusiness.controller.admin;

import com.ch.ebusiness.dto.*;
import com.ch.ebusiness.service.admin.StatisticsService;
import com.ch.ebusiness.service.MultiLevelCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 统计数据控制器
 */
@Controller
@RequestMapping("/statistics")
public class StatisticsController extends AdminBaseController {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private MultiLevelCacheService cacheService;

    /**
     * 跳转到仪表盘页面
     */
    @RequestMapping("/dashboard")
    public String dashboard(Model model) {
        // 添加缓存统计数据到模型
        List<CacheStatsDTO> cacheStats = cacheService.getCacheStats();
        model.addAttribute("cacheStats", cacheStats);
        return "admin/dashboard";
    }

    /**
     * 跳转到缓存管理页面
     */
    @RequestMapping("/cacheManagement")
    public String cacheManagement(Model model) {
        // 添加缓存统计数据到模型
        List<CacheStatsDTO> cacheStats = cacheService.getCacheStats();
        model.addAttribute("cacheStats", cacheStats);

        // 获取所有缓存键和常用模式
        List<String> allKeys = cacheService.getAllCacheKeys();
        List<String> patterns = cacheService.getCommonCachePatterns();
        model.addAttribute("cacheKeys", allKeys);
        model.addAttribute("cachePatterns", patterns);

        return "admin/cacheManagement";
    }

    /**
     * 获取仪表盘汇总数据
     */
    @GetMapping("/summary")
    @ResponseBody
    public DashboardSummaryDTO getSummary() {
        return statisticsService.getDashboardSummary();
    }

    /**
     * 获取销售趋势数据
     * 
     * @param period 时间周期：day/week/month
     * @param count  时间段数量
     */
    @GetMapping("/sales/trend")
    @ResponseBody
    public List<SalesTrendDTO> getSalesTrend(
            @RequestParam(defaultValue = "day") String period,
            @RequestParam(defaultValue = "30") Integer count) {
        return statisticsService.getSalesTrend(period, count);
    }

    /**
     * 获取商品销售排行
     * 
     * @param type  排行类型：count/amount
     * @param limit 限制数量
     */
    @GetMapping("/goods/rank")
    @ResponseBody
    public List<GoodsRankDTO> getGoodsRank(
            @RequestParam(defaultValue = "count") String type,
            @RequestParam(defaultValue = "10") Integer limit) {
        return statisticsService.getGoodsRank(type, limit);
    }

    /**
     * 获取商品类型销售统计
     */
    @GetMapping("/type/sales")
    @ResponseBody
    public List<TypeSalesDTO> getTypeSales() {
        return statisticsService.getTypeSales();
    }

    /**
     * 获取商品类型数量分布
     */
    @GetMapping("/type/goods-count")
    @ResponseBody
    public List<Map<String, Object>> getTypeGoodsCount() {
        return statisticsService.getTypeGoodsCount();
    }

    /**
     * 获取用户活跃度趋势
     * 
     * @param days 天数
     */
    @GetMapping("/user/activity")
    @ResponseBody
    public List<UserActivityDTO> getUserActivity(
            @RequestParam(defaultValue = "30") Integer days) {
        return statisticsService.getUserActivityTrend(days);
    }

    /**
     * 获取购物车转化率数据
     */
    @GetMapping("/cart/conversion")
    @ResponseBody
    public Map<String, Object> getCartConversion() {
        return statisticsService.getCartConversionData();
    }

    /**
     * 获取低库存商品列表
     * 
     * @param threshold 库存阈值
     */
    @GetMapping("/stock/warning")
    @ResponseBody
    public List<StockWarningDTO> getLowStock(
            @RequestParam(defaultValue = "10") Integer threshold) {
        return statisticsService.getLowStockGoods(threshold);
    }

    /**
     * 获取商品上下架状态分布
     */
    @GetMapping("/goods/status")
    @ResponseBody
    public List<Map<String, Object>> getGoodsStatus() {
        return statisticsService.getGoodsStatusDistribution();
    }

    /**
     * 获取订单状态分布
     */
    @GetMapping("/order/status")
    @ResponseBody
    public List<OrderStatusDTO> getOrderStatus() {
        return statisticsService.getOrderStatusDistribution();
    }

    /**
     * 获取管理员操作统计
     */
    @GetMapping("/admin/operation")
    @ResponseBody
    public Map<String, Object> getAdminOperation() {
        return statisticsService.getAdminOperationStats();
    }

    /**
     * 获取用户活跃时段热力图数据
     * 
     * @param days 统计天数
     */
    @GetMapping("/heatmap/activity")
    @ResponseBody
    public List<HourActivityDTO> getActivityHeatmap(
            @RequestParam(defaultValue = "30") Integer days) {
        return statisticsService.getHourActivityHeatmap(days);
    }

    /**
     * 获取价格与销量关系数据
     */
    @GetMapping("/price-sales")
    @ResponseBody
    public List<Map<String, Object>> getPriceVsSales() {
        return statisticsService.getPriceVsSalesData();
    }
}
