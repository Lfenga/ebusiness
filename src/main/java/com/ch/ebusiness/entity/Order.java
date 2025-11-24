package com.ch.ebusiness.entity;

import java.math.BigDecimal;

public class Order {
	private Integer id;
	private Integer busertable_id;
	private BigDecimal amount;
	private Integer status;
	private String orderdate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBusertable_id() {
		return busertable_id;
	}

	public void setBusertable_id(Integer busertable_id) {
		this.busertable_id = busertable_id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOrderdate() {
		return orderdate;
	}

	public void setOrderdate(String orderdate) {
		this.orderdate = orderdate;
	}
}
