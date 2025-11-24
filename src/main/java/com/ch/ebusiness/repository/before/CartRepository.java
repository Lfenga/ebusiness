package com.ch.ebusiness.repository.before;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ch.ebusiness.entity.Order;

@Mapper
public interface CartRepository {
	public int putCart(@Param("uid") Integer uid,
			@Param("gid") Integer gid,
			@Param("bnum") Integer bnum);

	public List<Map<String, Object>> isPutCart(@Param("uid") Integer uid, @Param("gid") Integer gid);

	public int updateCart(@Param("uid") Integer uid,
			@Param("gid") Integer gid,
			@Param("bnum") Integer bnum);

	public List<Map<String, Object>> selectCart(Integer uid);

	public int deleteAgoods(@Param("uid") Integer uid, @Param("gid") Integer gid);

	public int clear(Integer uid);

	public int addOrder(Order order);

	public int addOrderDetail(@Param("ordersn") Integer ordersn, @Param("uid") Integer uid);

	public List<Map<String, Object>> selectGoodsShop(Integer uid);

	public int updateStore(Map<String, Object> map);

	public int pay(Integer ordersn);

	public List<Map<String, Object>> myOrder(Integer uid);

	public int countMyOrder(Integer uid);

	public List<Map<String, Object>> myOrderByPage(@Param("uid") Integer uid,
			@Param("startIndex") int startIndex,
			@Param("pageSize") int pageSize);

	public List<Map<String, Object>> orderDetail(Integer id);

	public int updateUpwd(@Param("uid") Integer uid, @Param("bpwd") String bpwd);
}
