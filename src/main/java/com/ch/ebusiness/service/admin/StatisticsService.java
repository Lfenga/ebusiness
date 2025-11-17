package com.ch.ebusiness.service.admin;

import com.ch.ebusiness.dto.*;
import java.util.List;
import java.util.Map;

/**
 * 统计服务接口
 */
public interface StatisticsService {

    /**
     * 获取仪表盘汇总数据
     */
    DashboardSummaryDTO getDashboardSummary();

    /**
     * 获取销售趋势
     * 
     * @param period 时间周期：day/week/month
     * @param count  时间段数量
     */
    List<SalesTrendDTO> getSalesTrend(String period, Integer count);

    /**
     * 获取商品销售排行
     * 
     * @param type  排行类型：count/amount
     * @param limit 限制数量
     */
    List<GoodsRankDTO> getGoodsRank(String type, Integer limit);

    /**
     * 获取商品类型销售统计
     */
    List<TypeSalesDTO> getTypeSales();

    /**
     * 获取商品类型数量分布
     */
    List<Map<String, Object>> getTypeGoodsCount();

    /**
     * 获取用户活跃度趋势
     * 
     * @param days 天数
     */
    List<UserActivityDTO> getUserActivityTrend(Integer days);

    /**
     * 获取购物车转化率数据
     */
    Map<String, Object> getCartConversionData();

    /**
     * 获取低库存商品列表
     * 
     * @param threshold 库存阈值
     */
    List<StockWarningDTO> getLowStockGoods(Integer threshold);

    /**
     * 获取商品上下架状态分布
     */
    List<Map<String, Object>> getGoodsStatusDistribution();

    /**
     * 获取订单状态分布
     */
    List<OrderStatusDTO> getOrderStatusDistribution();

    /**
     * 获取管理员操作统计
     */
    Map<String, Object> getAdminOperationStats();

    /**
     * 获取用户活跃时段热力图数据
     * 
     * @param days 统计天数
     */
    List<HourActivityDTO> getHourActivityHeatmap(Integer days);

    /**
     * 获取价格与销量关系数据
     */
    List<Map<String, Object>> getPriceVsSalesData();
}
