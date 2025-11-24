package com.ch.ebusiness.entity;

import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;

public class Goods {
	private int id;
	private String gname;
	private BigDecimal goprice;
	private BigDecimal grprice;
	private int stock;
	private String gpicture;
	private MultipartFile fileName;
	private int goodstype_id;
	private String typename;
	private int buyNumber;// 加入购物车使用
	private int status; // 商品状态：1-上架，0-下架

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public BigDecimal getGoprice() {
		return goprice;
	}

	public void setGoprice(BigDecimal goprice) {
		this.goprice = goprice;
	}

	public BigDecimal getGrprice() {
		return grprice;
	}

	public void setGrprice(BigDecimal grprice) {
		this.grprice = grprice;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getGpicture() {
		return gpicture;
	}

	public void setGpicture(String gpicture) {
		this.gpicture = gpicture;
	}

	public int getGoodstype_id() {
		return goodstype_id;
	}

	public void setGoodstype_id(int goodstype_id) {
		this.goodstype_id = goodstype_id;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public int getBuyNumber() {
		return buyNumber;
	}

	public void setBuyNumber(int buyNumber) {
		this.buyNumber = buyNumber;
	}

	public MultipartFile getFileName() {
		return fileName;
	}

	public void setFileName(MultipartFile fileName) {
		this.fileName = fileName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
