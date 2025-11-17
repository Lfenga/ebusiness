package com.ch.ebusiness.dto;

/**
 * 商品销售排行DTO
 */
public class GoodsRankDTO {
    private Integer goodsId; // 商品ID
    private String goodsName; // 商品名称
    private Long salesCount; // 销售数量
    private Double salesAmount; // 销售额
    private String typeName; // 商品类型

    public GoodsRankDTO() {
    }

    // Getters and Setters
    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
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

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
