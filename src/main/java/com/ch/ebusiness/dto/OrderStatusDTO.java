package com.ch.ebusiness.dto;

/**
 * 订单状态分布DTO
 */
public class OrderStatusDTO {
    private Integer status; // 状态码
    private String statusName; // 状态名称
    private Long count; // 数量

    public OrderStatusDTO() {
    }

    public OrderStatusDTO(Integer status, String statusName, Long count) {
        this.status = status;
        this.statusName = statusName;
        this.count = count;
    }

    // Getters and Setters
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
