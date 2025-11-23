package com.ch.ebusiness.service;

import com.ch.ebusiness.dto.CacheStatsDTO;

import java.util.List;
import java.util.function.Supplier;

/**
 * 多级缓存服务接口
 * 提供统一的缓存操作API，支持Caffeine+Redis两级缓存
 */
public interface MultiLevelCacheService {

    /**
     * 从缓存获取数据，采用Caffeine -> Redis -> Database的查询顺序
     * 
     * @param cacheKey   缓存键
     * @param dataLoader 数据加载器（当缓存未命中时从数据库加载）
     * @param <T>        数据类型
     * @return 缓存或数据库中的数据
     */
    <T> T get(String cacheKey, Supplier<T> dataLoader);

    /**
     * 将数据同时写入两级缓存
     * 
     * @param cacheKey 缓存键
     * @param value    缓存值
     */
    void put(String cacheKey, Object value);

    /**
     * 清除指定键的所有级别缓存
     * 
     * @param cacheKey 缓存键
     */
    void evict(String cacheKey);

    /**
     * 清除指定模式的所有缓存键
     * 
     * @param keyPattern 键模式（如：index:lasted_goods:tid_*）
     */
    void evictByPattern(String keyPattern);

    /**
     * 清空所有缓存
     */
    void evictAll();

    /**
     * 获取所有缓存的统计信息
     * 
     * @return 缓存统计列表
     */
    List<CacheStatsDTO> getCacheStats();

    /**
     * 检查缓存键是否存在
     * 
     * @param cacheKey 缓存键
     * @return 是否存在
     */
    boolean exists(String cacheKey);

    /**
     * 获取缓存的剩余过期时间（秒）
     * 
     * @param cacheKey 缓存键
     * @return 剩余秒数，-1表示永不过期，-2表示不存在
     */
    long getExpire(String cacheKey);

    /**
     * 获取所有缓存键列表
     * 
     * @return 缓存键列表
     */
    List<String> getAllCacheKeys();

    /**
     * 获取所有常用缓存模式
     * 
     * @return 缓存模式列表
     */
    List<String> getCommonCachePatterns();
}
