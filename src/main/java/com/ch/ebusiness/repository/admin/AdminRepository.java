package com.ch.ebusiness.repository.admin;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ch.ebusiness.entity.AUser;

@Mapper
public interface AdminRepository {
	List<AUser> login(AUser aUser);

	/**
	 * 根据用户名查询管理员
	 */
	AUser selectByAname(@Param("aname") String aname);

	/**
	 * 查询所有管理员（分页）
	 */
	List<AUser> selectAllAdmins(@Param("offset") int offset, @Param("limit") int limit);

	/**
	 * 统计管理员总数
	 */
	int countAllAdmins();

	/**
	 * 插入新管理员
	 */
	int insertAdmin(AUser admin);

	/**
	 * 更新管理员信息
	 */
	int updateAdmin(AUser admin);

	/**
	 * 更新管理员登录时间
	 */
	int updateLastLoginTime(@Param("id") Integer id);

	/**
	 * 删除管理员
	 */
	int deleteAdmin(@Param("id") Integer id);
}
