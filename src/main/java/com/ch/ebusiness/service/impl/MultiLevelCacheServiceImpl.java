package com.ch.ebusiness.service.impl;

import com.ch.ebusiness.dto.CacheStatsDTO;
import com.ch.ebusiness.service.MultiLevelCacheService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 多级缓存服务实现类
 * 实现Caffeine(L1) -> Redis(L2) -> Database的查询逻辑
 */
@Service
public class MultiLevelCacheServiceImpl implements MultiLevelCacheService {

    private static final Logger logger = LoggerFactory.getLogger(MultiLevelCacheServiceImpl.class);

    @Autowired
    @Qualifier("caffeineCacheManager")
    private CacheManager caffeineCacheManager;

    @Autowired
    @Qualifier("redisCacheManager")
    private CacheManager redisCacheManager;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 缓存命中统计
    private long caffeineHitCount = 0;
    private long caffeineMissCount = 0;
    private long redisHitCount = 0;
    private long redisMissCount = 0;
    private long dbQueryCount = 0;

    /**
     * 获取数据：Caffeine -> Redis -> Database
     */
    @Override
    public <T> T get(String cacheKey, Supplier<T> dataLoader) {
        long startTime = System.currentTimeMillis();

        // 1. 尝试从Caffeine缓存获取
        T value = getFromCaffeine(cacheKey);
        if (value != null) {
            caffeineHitCount++;
            logger.debug("缓存命中[L1-Caffeine]: {} , 耗时: {}ms",
                    cacheKey, System.currentTimeMillis() - startTime);
            return value;
        }
        caffeineMissCount++;

        // 2. Caffeine未命中，尝试从Redis获取
        value = getFromRedis(cacheKey);
        if (value != null) {
            redisHitCount++;
            // 回填Caffeine缓存
            putToCaffeine(cacheKey, value);
            logger.debug("缓存命中[L2-Redis]: {}, 耗时: {}ms",
                    cacheKey, System.currentTimeMillis() - startTime);
            return value;
        }
        redisMissCount++;

        // 3. 两级缓存都未命中，从数据库加载
        value = dataLoader.get();
        dbQueryCount++;

        if (value != null) {
            // 同时写入两级缓存
            put(cacheKey, value);
            logger.info("缓存未命中，从数据库加载: {}, 耗时: {}ms",
                    cacheKey, System.currentTimeMillis() - startTime);
        }

        return value;
    }

    /**
     * 写入两级缓存
     */
    @Override
    public void put(String cacheKey, Object value) {
        if (value != null) {
            putToCaffeine(cacheKey, value);
            putToRedis(cacheKey, value);
            logger.debug("数据已写入两级缓存: {}", cacheKey);
        }
    }

    /**
     * 清除指定键的所有级别缓存
     */
    @Override
    public void evict(String cacheKey) {
        evictFromCaffeine(cacheKey);
        evictFromRedis(cacheKey);
        logger.info("已清除缓存: {}", cacheKey);
    }

    /**
     * 清除匹配模式的所有缓存键
     */
    @Override
    public void evictByPattern(String keyPattern) {
        // 清除Redis中匹配的键
        Set<String> keys = redisTemplate.keys(keyPattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            logger.info("已清除Redis中匹配模式的缓存: {}, 数量: {}", keyPattern, keys.size());
        }

        // Caffeine不支持模式匹配，清空所有缓存
        clearAllCaffeineCache();
        logger.info("已清空所有Caffeine缓存");
    }

    /**
     * 清空所有缓存
     */
    @Override
    public void evictAll() {
        clearAllCaffeineCache();
        clearAllRedisCache();
        logger.warn("已清空所有两级缓存");
    }

    /**
     * 获取缓存统计信息
     */
    @Override
    public List<CacheStatsDTO> getCacheStats() {
        List<CacheStatsDTO> statsList = new ArrayList<>();

        // Caffeine统计
        CacheStatsDTO caffeineStats = new CacheStatsDTO("MultiLevel", "L1-Caffeine");
        caffeineStats.setHitCount(caffeineHitCount);
        caffeineStats.setMissCount(caffeineMissCount);
        caffeineStats.calculateHitRate();
        caffeineStats.setCacheSize(getCaffeineSize());
        statsList.add(caffeineStats);

        // Redis统计
        CacheStatsDTO redisStats = new CacheStatsDTO("MultiLevel", "L2-Redis");
        redisStats.setHitCount(redisHitCount);
        redisStats.setMissCount(redisMissCount);
        redisStats.calculateHitRate();
        redisStats.setCacheSize(getRedisSize());
        statsList.add(redisStats);

        // 数据库查询统计
        CacheStatsDTO dbStats = new CacheStatsDTO("MultiLevel", "Database");
        dbStats.setRequestCount(dbQueryCount);
        statsList.add(dbStats);

        return statsList;
    }

    /**
     * 检查缓存键是否存在
     */
    @Override
    public boolean exists(String cacheKey) {
        Boolean exists = redisTemplate.hasKey(cacheKey);
        return exists != null && exists;
    }

    /**
     * 获取缓存剩余过期时间
     */
    @Override
    public long getExpire(String cacheKey) {
        Long expire = redisTemplate.getExpire(cacheKey, TimeUnit.SECONDS);
        return expire != null ? expire : -2;
    }

    // ============ 私有辅助方法 ============

    @SuppressWarnings("unchecked")
    private <T> T getFromCaffeine(String cacheKey) {
        try {
            org.springframework.cache.Cache cache = caffeineCacheManager.getCache("default");
            if (cache != null) {
                org.springframework.cache.Cache.ValueWrapper wrapper = cache.get(cacheKey);
                if (wrapper != null) {
                    return (T) wrapper.get();
                }
            }
        } catch (Exception e) {
            logger.error("从Caffeine获取缓存失败: {}", cacheKey, e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> T getFromRedis(String cacheKey) {
        try {
            return (T) redisTemplate.opsForValue().get(cacheKey);
        } catch (Exception e) {
            logger.error("从Redis获取缓存失败: {}", cacheKey, e);
            return null;
        }
    }

    private void putToCaffeine(String cacheKey, Object value) {
        try {
            org.springframework.cache.Cache cache = caffeineCacheManager.getCache("default");
            if (cache != null) {
                cache.put(cacheKey, value);
            }
        } catch (Exception e) {
            logger.error("写入Caffeine缓存失败: {}", cacheKey, e);
        }
    }

    private void putToRedis(String cacheKey, Object value) {
        try {
            // 根据缓存键设置不同的过期时间
            long ttl = determineTtl(cacheKey);
            redisTemplate.opsForValue().set(cacheKey, value, ttl, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("写入Redis缓存失败: {}", cacheKey, e);
        }
    }

    private void evictFromCaffeine(String cacheKey) {
        try {
            org.springframework.cache.Cache cache = caffeineCacheManager.getCache("default");
            if (cache != null) {
                cache.evict(cacheKey);
            }
        } catch (Exception e) {
            logger.error("清除Caffeine缓存失败: {}", cacheKey, e);
        }
    }

    private void evictFromRedis(String cacheKey) {
        try {
            redisTemplate.delete(cacheKey);
        } catch (Exception e) {
            logger.error("清除Redis缓存失败: {}", cacheKey, e);
        }
    }

    private void clearAllCaffeineCache() {
        try {
            org.springframework.cache.Cache cache = caffeineCacheManager.getCache("default");
            if (cache != null) {
                cache.clear();
            }
        } catch (Exception e) {
            logger.error("清空Caffeine缓存失败", e);
        }
    }

    private void clearAllRedisCache() {
        try {
            Set<String> keys = redisTemplate.keys("*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        } catch (Exception e) {
            logger.error("清空Redis缓存失败", e);
        }
    }

    private long getCaffeineSize() {
        try {
            org.springframework.cache.Cache cache = caffeineCacheManager.getCache("default");
            if (cache instanceof CaffeineCache) {
                Cache<Object, Object> nativeCache = ((CaffeineCache) cache).getNativeCache();
                return nativeCache.estimatedSize();
            }
        } catch (Exception e) {
            logger.error("获取Caffeine缓存大小失败", e);
        }
        return 0;
    }

    private long getRedisSize() {
        try {
            Set<String> keys = redisTemplate.keys("*");
            return keys != null ? keys.size() : 0;
        } catch (Exception e) {
            logger.error("获取Redis缓存大小失败", e);
            return 0;
        }
    }

    /**
     * 根据缓存键前缀确定过期时间
     */
    private long determineTtl(String cacheKey) {
        if (cacheKey.startsWith("index:goods_types")) {
            return 86400; // 24小时
        } else if (cacheKey.startsWith("index:lasted_goods")) {
            return 3600; // 1小时
        } else if (cacheKey.startsWith("goods:detail")) {
            return 7200; // 2小时
        } else if (cacheKey.startsWith("index:search")) {
            return 1800; // 30分钟
        }
        return 3600; // 默认1小时
    }

    /**
     * 获取所有缓存键
     */
    @Override
    public List<String> getAllCacheKeys() {
        List<String> keys = new ArrayList<>();
        try {
            Set<String> redisKeys = redisTemplate.keys("*");
            if (redisKeys != null) {
                keys.addAll(redisKeys);
            }
        } catch (Exception e) {
            logger.error("获取缓存键列表失败", e);
        }
        return keys;
    }

    /**
     * 获取常用缓存模式
     */
    @Override
    public List<String> getCommonCachePatterns() {
        List<String> patterns = new ArrayList<>();
        patterns.add("index:lasted_goods:*");
        patterns.add("goods:detail:*");
        patterns.add("index:search:*");
        patterns.add("index:*");
        patterns.add("goods:*");
        return patterns;
    }
}
