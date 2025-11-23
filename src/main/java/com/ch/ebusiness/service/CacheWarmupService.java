package com.ch.ebusiness.service;

import com.ch.ebusiness.entity.Goods;
import com.ch.ebusiness.entity.GoodsType;
import com.ch.ebusiness.repository.before.IndexRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 缓存预热服务
 * 在应用启动时自动加载热门数据到缓存中
 */
@Component
@Order(100) // 设置较低优先级，确保其他Bean已初始化
public class CacheWarmupService implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(CacheWarmupService.class);

    @Autowired
    private MultiLevelCacheService cacheService;

    @Autowired
    private IndexRepository indexRepository;

    @Override
    public void run(String... args) throws Exception {
        logger.info("========== 开始缓存预热 ==========");
        long startTime = System.currentTimeMillis();

        try {
            // 1. 预热商品类型数据（访问频率最高）
            warmupGoodsTypes();

            // 2. 预热首页商品列表（所有类型）
            warmupLastedGoods();

            // 3. 预热热门商品详情（可选，取前10个商品）
            warmupHotGoods();

            long duration = System.currentTimeMillis() - startTime;
            logger.info("========== 缓存预热完成，耗时: {}ms ==========", duration);
        } catch (Exception e) {
            logger.error("缓存预热过程中发生错误", e);
        }
    }

    /**
     * 预热商品类型数据
     */
    private void warmupGoodsTypes() {
        try {
            logger.info("预热商品类型数据...");
            List<GoodsType> goodsTypes = indexRepository.selectGoodsType();
            if (goodsTypes != null && !goodsTypes.isEmpty()) {
                cacheService.put("index:goods_types", goodsTypes);
                logger.info("商品类型数据预热成功，共{}个类型", goodsTypes.size());
            }
        } catch (Exception e) {
            logger.error("预热商品类型数据失败", e);
        }
    }

    /**
     * 预热首页商品列表
     */
    private void warmupLastedGoods() {
        try {
            logger.info("预热首页商品列表...");

            // 预热全部商品（tid=0）
            List<Goods> allGoods = indexRepository.selectLastedGoods(0);
            if (allGoods != null && !allGoods.isEmpty()) {
                cacheService.put("index:lasted_goods:tid_0", allGoods);
                logger.info("全部商品列表预热成功，共{}个商品", allGoods.size());
            }

            // 预热各分类的商品列表
            List<GoodsType> types = indexRepository.selectGoodsType();
            if (types != null) {
                for (GoodsType type : types) {
                    List<Goods> typeGoods = indexRepository.selectLastedGoods(type.getId());
                    if (typeGoods != null && !typeGoods.isEmpty()) {
                        cacheService.put("index:lasted_goods:tid_" + type.getId(), typeGoods);
                        logger.debug("类型{}的商品列表预热成功，共{}个商品",
                                type.getTypename(), typeGoods.size());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("预热首页商品列表失败", e);
        }
    }

    /**
     * 预热热门商品详情
     */
    private void warmupHotGoods() {
        try {
            logger.info("预热热门商品详情...");

            // 获取最新的商品作为热门商品
            List<Goods> lastedGoods = indexRepository.selectLastedGoods(0);
            if (lastedGoods != null && !lastedGoods.isEmpty()) {
                // 只预热前10个商品的详情
                int limit = Math.min(10, lastedGoods.size());
                for (int i = 0; i < limit; i++) {
                    Goods goods = lastedGoods.get(i);
                    Goods detail = indexRepository.selectAGoods(goods.getId());
                    if (detail != null) {
                        cacheService.put("goods:detail:" + goods.getId(), detail);
                    }
                }
                logger.info("热门商品详情预热成功，共{}个商品", limit);
            }
        } catch (Exception e) {
            logger.error("预热热门商品详情失败", e);
        }
    }

    /**
     * 手动触发缓存预热（用于管理后台手动刷新）
     */
    public void manualWarmup() {
        logger.info("手动触发缓存预热");
        try {
            run();
        } catch (Exception e) {
            logger.error("手动缓存预热失败", e);
            throw new RuntimeException("缓存预热失败: " + e.getMessage());
        }
    }
}
