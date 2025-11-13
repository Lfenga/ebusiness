package com.ch.ebusiness.repository.before;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ch.ebusiness.entity.Goods;
import com.ch.ebusiness.entity.GoodsType;

@Mapper
public interface IndexRepository {
	public List<GoodsType> selectGoodsType();

	public List<Goods> selectLastedGoods(Integer tid);

	public Goods selectAGoods(Integer id);

	public List<Goods> search(String mykey);
}
