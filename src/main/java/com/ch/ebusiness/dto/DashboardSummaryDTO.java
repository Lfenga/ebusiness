package com.ch.ebusiness.dto;

/**
 * 仪表盘汇总数据DTO
 */
public class DashboardSummaryDTO {
    private Long totalUsers; // 总用户数
    private Long totalOrders; // 总订单数
    private Double totalSales; // 总销售额
    private Long todayOrders; // 今日订单数
    private Double todaySales; // 今日销售额
    private Long newUsersToday; // 今日新增用户
    private Double conversionRate; // 购物车转化率
    private Long lowStockCount; // 低库存商品数

    public DashboardSummaryDTO() {
    }

    // Getters and Setters
    public Long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Double totalSales) {
        this.totalSales = totalSales;
    }

    public Long getTodayOrders() {
        return todayOrders;
    }

    public void setTodayOrders(Long todayOrders) {
        this.todayOrders = todayOrders;
    }

    public Double getTodaySales() {
        return todaySales;
    }

    public void setTodaySales(Double todaySales) {
        this.todaySales = todaySales;
    }

    public Long getNewUsersToday() {
        return newUsersToday;
    }

    public void setNewUsersToday(Long newUsersToday) {
        this.newUsersToday = newUsersToday;
    }

    public Double getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(Double conversionRate) {
        this.conversionRate = conversionRate;
    }

    public Long getLowStockCount() {
        return lowStockCount;
    }

    public void setLowStockCount(Long lowStockCount) {
        this.lowStockCount = lowStockCount;
    }
}
