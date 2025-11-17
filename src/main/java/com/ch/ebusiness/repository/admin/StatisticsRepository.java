package com.ch.ebusiness.repository.admin;

import com.ch.ebusiness.dto.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 统计数据Repository
 */
@Repository
public interface StatisticsRepository {

    // ==================== 仪表盘汇总数据 ====================

    /**
     * 获取总用户数
     */
    Long getTotalUsers();

    /**
     * 获取总订单数
     */
    Long getTotalOrders();

    /**
     * 获取总销售额
     */
    Double getTotalSales();

    /**
     * 获取今日订单数
     */
    Long getTodayOrders();

    /**
     * 获取今日销售额
     */
    Double getTodaySales();

    /**
     * 获取今日新增用户数
     */
    Long getNewUsersToday();

    /**
     * 获取购物车用户数
     */
    Long getCartUsers();

    /**
     * 获取下单用户数
     */
    Long getOrderUsers();

    /**
     * 获取低库存商品数（库存<10）
     */
    Long getLowStockCount(@Param("threshold") Integer threshold);

    // ==================== 销售趋势分析 ====================

    /**
     * 获取销售趋势（按日）
     */
    List<SalesTrendDTO> getSalesTrendByDay(@Param("days") Integer days);

    /**
     * 获取销售趋势（按周）
     */
    List<SalesTrendDTO> getSalesTrendByWeek(@Param("weeks") Integer weeks);

    /**
     * 获取销售趋势（按月）
     */
    List<SalesTrendDTO> getSalesTrendByMonth(@Param("months") Integer months);

    // ==================== 商品销售排行 ====================

    /**
     * 获取商品销售排行（按数量）
     */
    List<GoodsRankDTO> getGoodsRankByCount(@Param("limit") Integer limit);

    /**
     * 获取商品销售排行（按销售额）
     */
    List<GoodsRankDTO> getGoodsRankByAmount(@Param("limit") Integer limit);

    // ==================== 商品类型分析 ====================

    /**
     * 获取商品类型销售统计
     */
    List<TypeSalesDTO> getTypeSales();

    /**
     * 获取商品类型数量分布
     */
    List<Map<String, Object>> getTypeGoodsCount();

    // ==================== 用户行为分析 ====================

    /**
     * 获取用户活跃度趋势
     */
    List<UserActivityDTO> getUserActivityTrend(@Param("days") Integer days);

    // ==================== 库存管理 ====================

    /**
     * 获取低库存商品列表
     */
    List<StockWarningDTO> getLowStockGoods(@Param("threshold") Integer threshold);

    /**
     * 获取商品上下架状态分布
     */
    List<Map<String, Object>> getGoodsStatusDistribution();

    // ==================== 订单分析 ====================

    /**
     * 获取订单状态分布
     */
    List<OrderStatusDTO> getOrderStatusDistribution();

    // ==================== 管理员操作统计 ====================

    /**
     * 获取管理员操作类型分布
     */
    List<Map<String, Object>> getAdminOperationTypeDistribution();

    /**
     * 获取管理员操作模块分布
     */
    List<Map<String, Object>> getAdminOperationModuleDistribution();

    // ==================== 热力图数据 ====================

    /**
     * 获取用户活跃时段分布（小时×星期）
     */
    List<HourActivityDTO> getHourActivityHeatmap(@Param("days") Integer days);

    // ==================== 价格与销量关系 ====================

    /**
     * 获取商品价格与销量数据
     */
    List<Map<String, Object>> getPriceVsSalesData();
}
