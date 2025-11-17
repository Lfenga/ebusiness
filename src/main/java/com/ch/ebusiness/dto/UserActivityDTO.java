package com.ch.ebusiness.dto;

/**
 * 用户活跃度分析DTO
 */
public class UserActivityDTO {
    private String date; // 日期
    private Long newUsers; // 新增用户数
    private Long activeUsers; // 活跃用户数（下单用户）

    public UserActivityDTO() {
    }

    public UserActivityDTO(String date, Long newUsers, Long activeUsers) {
        this.date = date;
        this.newUsers = newUsers;
        this.activeUsers = activeUsers;
    }

    // Getters and Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getNewUsers() {
        return newUsers;
    }

    public void setNewUsers(Long newUsers) {
        this.newUsers = newUsers;
    }

    public Long getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(Long activeUsers) {
        this.activeUsers = activeUsers;
    }
}
