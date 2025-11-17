package com.ch.ebusiness.dto;

/**
 * 销售趋势数据DTO
 */
public class SalesTrendDTO {
    private String date; // 日期
    private Long orderCount; // 订单数量
    private Double salesAmount; // 销售额

    public SalesTrendDTO() {
    }

    public SalesTrendDTO(String date, Long orderCount, Double salesAmount) {
        this.date = date;
        this.orderCount = orderCount;
        this.salesAmount = salesAmount;
    }

    // Getters and Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }

    public Double getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(Double salesAmount) {
        this.salesAmount = salesAmount;
    }
}
