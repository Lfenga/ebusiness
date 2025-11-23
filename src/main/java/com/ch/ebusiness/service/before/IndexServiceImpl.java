package com.ch.ebusiness.service.before;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.ch.ebusiness.entity.Goods;
import com.ch.ebusiness.entity.GoodsType;
import com.ch.ebusiness.repository.before.IndexRepository;
import com.ch.ebusiness.service.MultiLevelCacheService;

import java.util.List;

@Service
public class IndexServiceImpl implements IndexService {
	private static final Logger logger = LoggerFactory.getLogger(IndexServiceImpl.class);

	@Autowired
	private IndexRepository indexRepository;

	@Autowired
	private MultiLevelCacheService cacheService;

	// 缓存键常量
	private static final String CACHE_KEY_GOODS_TYPES = "index:goods_types";
	private static final String CACHE_KEY_LASTED_GOODS = "index:lasted_goods:tid_";
	private static final String CACHE_KEY_GOODS_DETAIL = "goods:detail:";
	private static final String CACHE_KEY_SEARCH = "index:search:";

	@Override
	public String index(Model model, Integer tid) {
		if (tid == null)
			tid = 0;

		// 从缓存获取商品类型
		List<GoodsType> goodsTypes = cacheService.get(
				CACHE_KEY_GOODS_TYPES,
				() -> {
					logger.info("从数据库加载商品类型列表");
					return indexRepository.selectGoodsType();
				});
		model.addAttribute("goodsType", goodsTypes);

		// 从缓存获取最新商品
		final Integer finalTid = tid;
		List<Goods> lastedGoods = cacheService.get(
				CACHE_KEY_LASTED_GOODS + tid,
				() -> {
					logger.info("从数据库加载最新商品，类型ID: {}", finalTid);
					return indexRepository.selectLastedGoods(finalTid);
				});
		model.addAttribute("lastedGoods", lastedGoods);

		return "user/index";
	}

	@Override
	public String goodsDetail(Model model, Integer id) {
		// 从缓存获取商品类型
		List<GoodsType> goodsTypes = cacheService.get(
				CACHE_KEY_GOODS_TYPES,
				() -> {
					logger.info("从数据库加载商品类型列表");
					return indexRepository.selectGoodsType();
				});
		model.addAttribute("goodsType", goodsTypes);

		// 从缓存获取商品详情
		Goods goods = cacheService.get(
				CACHE_KEY_GOODS_DETAIL + id,
				() -> {
					logger.info("从数据库加载商品详情，商品ID: {}", id);
					return indexRepository.selectAGoods(id);
				});
		model.addAttribute("goods", goods);

		return "user/goodsDetail";
	}

	@Override
	public String search(Model model, String mykey) {
		// 从缓存获取商品类型
		List<GoodsType> goodsTypes = cacheService.get(
				CACHE_KEY_GOODS_TYPES,
				() -> {
					logger.info("从数据库加载商品类型列表");
					return indexRepository.selectGoodsType();
				});
		model.addAttribute("goodsType", goodsTypes);

		// 从缓存获取搜索结果（对关键词进行简单处理避免特殊字符）
		String cacheKey = CACHE_KEY_SEARCH
				+ (mykey != null ? mykey.replaceAll("[^a-zA-Z0-9\\u4e00-\\u9fa5]", "_") : "empty");
		List<Goods> searchGoods = cacheService.get(
				cacheKey,
				() -> {
					logger.info("从数据库搜索商品，关键词: {}", mykey);
					return indexRepository.search(mykey);
				});
		model.addAttribute("searchgoods", searchGoods);

		return "user/searchResult";
	}
}
