package com.ch.ebusiness.service.admin;

import com.ch.ebusiness.dto.*;
import com.ch.ebusiness.repository.admin.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计服务实现
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Override
    public DashboardSummaryDTO getDashboardSummary() {
        DashboardSummaryDTO summary = new DashboardSummaryDTO();

        // 基础统计
        summary.setTotalUsers(statisticsRepository.getTotalUsers());
        summary.setTotalOrders(statisticsRepository.getTotalOrders());
        summary.setTotalSales(statisticsRepository.getTotalSales());
        summary.setTodayOrders(statisticsRepository.getTodayOrders());
        summary.setTodaySales(statisticsRepository.getTodaySales());
        summary.setNewUsersToday(statisticsRepository.getNewUsersToday());
        summary.setLowStockCount(statisticsRepository.getLowStockCount(10));

        // 计算购物车转化率
        Long cartUsers = statisticsRepository.getCartUsers();
        Long orderUsers = statisticsRepository.getOrderUsers();
        if (cartUsers != null && cartUsers > 0) {
            double rate = (orderUsers * 100.0) / cartUsers;
            summary.setConversionRate(Math.round(rate * 100.0) / 100.0);
        } else {
            summary.setConversionRate(0.0);
        }

        return summary;
    }

    @Override
    public List<SalesTrendDTO> getSalesTrend(String period, Integer count) {
        if (count == null || count <= 0) {
            count = 30; // 默认30天
        }

        switch (period.toLowerCase()) {
            case "week":
                return statisticsRepository.getSalesTrendByWeek(count);
            case "month":
                return statisticsRepository.getSalesTrendByMonth(count);
            case "day":
            default:
                return statisticsRepository.getSalesTrendByDay(count);
        }
    }

    @Override
    public List<GoodsRankDTO> getGoodsRank(String type, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10; // 默认前10名
        }

        if ("amount".equalsIgnoreCase(type)) {
            return statisticsRepository.getGoodsRankByAmount(limit);
        } else {
            return statisticsRepository.getGoodsRankByCount(limit);
        }
    }

    @Override
    public List<TypeSalesDTO> getTypeSales() {
        List<TypeSalesDTO> typeSales = statisticsRepository.getTypeSales();

        // 计算总销售额
        double totalSales = typeSales.stream()
                .mapToDouble(TypeSalesDTO::getSalesAmount)
                .sum();

        // 计算占比
        if (totalSales > 0) {
            typeSales.forEach(item -> {
                double percentage = (item.getSalesAmount() * 100.0) / totalSales;
                item.setPercentage(Math.round(percentage * 100.0) / 100.0);
            });
        }

        return typeSales;
    }

    @Override
    public List<Map<String, Object>> getTypeGoodsCount() {
        return statisticsRepository.getTypeGoodsCount();
    }

    @Override
    public List<UserActivityDTO> getUserActivityTrend(Integer days) {
        if (days == null || days <= 0) {
            days = 30; // 默认30天
        }
        return statisticsRepository.getUserActivityTrend(days);
    }

    @Override
    public Map<String, Object> getCartConversionData() {
        Map<String, Object> data = new HashMap<>();

        Long cartUsers = statisticsRepository.getCartUsers();
        Long orderUsers = statisticsRepository.getOrderUsers();

        data.put("cartUsers", cartUsers);
        data.put("orderUsers", orderUsers);

        if (cartUsers != null && cartUsers > 0) {
            double rate = (orderUsers * 100.0) / cartUsers;
            data.put("conversionRate", Math.round(rate * 100.0) / 100.0);
        } else {
            data.put("conversionRate", 0.0);
        }

        return data;
    }

    @Override
    public List<StockWarningDTO> getLowStockGoods(Integer threshold) {
        if (threshold == null || threshold <= 0) {
            threshold = 10; // 默认库存<10为低库存
        }
        return statisticsRepository.getLowStockGoods(threshold);
    }

    @Override
    public List<Map<String, Object>> getGoodsStatusDistribution() {
        return statisticsRepository.getGoodsStatusDistribution();
    }

    @Override
    public List<OrderStatusDTO> getOrderStatusDistribution() {
        return statisticsRepository.getOrderStatusDistribution();
    }

    @Override
    public Map<String, Object> getAdminOperationStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("operationType", statisticsRepository.getAdminOperationTypeDistribution());
        stats.put("operationModule", statisticsRepository.getAdminOperationModuleDistribution());
        return stats;
    }

    @Override
    public List<HourActivityDTO> getHourActivityHeatmap(Integer days) {
        if (days == null || days <= 0) {
            days = 30; // 默认30天
        }
        return statisticsRepository.getHourActivityHeatmap(days);
    }

    @Override
    public List<Map<String, Object>> getPriceVsSalesData() {
        return statisticsRepository.getPriceVsSalesData();
    }
}
