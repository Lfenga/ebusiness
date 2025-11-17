package com.ch.ebusiness.dto;

/**
 * 库存预警DTO
 */
public class StockWarningDTO {
    private Integer goodsId; // 商品ID
    private String goodsName; // 商品名称
    private Integer stock; // 库存数量
    private String typeName; // 商品类型
    private Integer status; // 上架状态

    public StockWarningDTO() {
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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
