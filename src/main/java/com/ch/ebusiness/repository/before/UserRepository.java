package com.ch.ebusiness.repository.before;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ch.ebusiness.entity.BUser;

@Mapper
public interface UserRepository {
	/**
	 * 根据邮箱查找用户
	 */
	public BUser selectByEmail(String bemail);

	/**
	 * 根据邮箱修改密码
	 */
	public int updatePasswordByEmail(String bemail, String newPassword);

	public List<BUser> isUse(BUser bUser);

	public int register(BUser bUser);

	public List<BUser> login(BUser bUser);
}
