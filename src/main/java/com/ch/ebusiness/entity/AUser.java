package com.ch.ebusiness.entity;

import java.util.Date;

/**
 * 管理员实体类
 */
public class AUser {
	private Integer id;
	private String aname;
	private String apwd;
	private String aemail;
	private String arealname;
	private String aphone;
	private Integer astatus;
	private Date createTime;
	private Date lastLoginTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAname() {
		return aname;
	}

	public void setAname(String aname) {
		this.aname = aname;
	}

	public String getApwd() {
		return apwd;
	}

	public void setApwd(String apwd) {
		this.apwd = apwd;
	}

	public String getAemail() {
		return aemail;
	}

	public void setAemail(String aemail) {
		this.aemail = aemail;
	}

	public String getArealname() {
		return arealname;
	}

	public void setArealname(String arealname) {
		this.arealname = arealname;
	}

	public String getAphone() {
		return aphone;
	}

	public void setAphone(String aphone) {
		this.aphone = aphone;
	}

	public Integer getAstatus() {
		return astatus;
	}

	public void setAstatus(Integer astatus) {
		this.astatus = astatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
}
