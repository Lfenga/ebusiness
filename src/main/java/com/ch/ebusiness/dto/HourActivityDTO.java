package com.ch.ebusiness.dto;

/**
 * 小时活跃度DTO（热力图用）
 */
public class HourActivityDTO {
    private Integer hour; // 小时 (0-23)
    private Integer weekday; // 星期 (1-7, 1=周一)
    private Long orderCount; // 订单数量

    public HourActivityDTO() {
    }

    public HourActivityDTO(Integer hour, Integer weekday, Long orderCount) {
        this.hour = hour;
        this.weekday = weekday;
        this.orderCount = orderCount;
    }

    // Getters and Setters
    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getWeekday() {
        return weekday;
    }

    public void setWeekday(Integer weekday) {
        this.weekday = weekday;
    }

    public Long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }
}
