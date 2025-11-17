package com.ch.ebusiness.dto;

/**
 * 商品类型销售占比DTO
 */
public class TypeSalesDTO {
    private Integer typeId; // 类型ID
    private String typeName; // 类型名称
    private Long salesCount; // 销售数量
    private Double salesAmount; // 销售额
    private Double percentage; // 占比

    public TypeSalesDTO() {
    }

    // Getters and Setters
    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Long getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(Long salesCount) {
        this.salesCount = salesCount;
    }

    public Double getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(Double salesAmount) {
        this.salesAmount = salesAmount;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}
