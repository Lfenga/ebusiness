package com.ch.ebusiness.dto;

/**
 * 缓存统计数据传输对象
 * 用于监控和展示缓存性能指标
 */
public class CacheStatsDTO {
    private String cacheName; // 缓存名称
    private String cacheLevel; // 缓存级别（L1-Caffeine, L2-Redis）
    private long hitCount; // 命中次数
    private long missCount; // 未命中次数
    private double hitRate; // 命中率（百分比）
    private long requestCount; // 总请求次数
    private long evictionCount; // 驱逐次数
    private long cacheSize; // 缓存条目数
    private double averageLoadPenalty; // 平均加载耗时（毫秒）
    private long memorySize; // 内存占用（字节）

    public CacheStatsDTO() {
    }

    public CacheStatsDTO(String cacheName, String cacheLevel) {
        this.cacheName = cacheName;
        this.cacheLevel = cacheLevel;
    }

    // Getters and Setters
    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getCacheLevel() {
        return cacheLevel;
    }

    public void setCacheLevel(String cacheLevel) {
        this.cacheLevel = cacheLevel;
    }

    public long getHitCount() {
        return hitCount;
    }

    public void setHitCount(long hitCount) {
        this.hitCount = hitCount;
    }

    public long getMissCount() {
        return missCount;
    }

    public void setMissCount(long missCount) {
        this.missCount = missCount;
    }

    public double getHitRate() {
        return hitRate;
    }

    public void setHitRate(double hitRate) {
        this.hitRate = hitRate;
    }

    public long getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(long requestCount) {
        this.requestCount = requestCount;
    }

    public long getEvictionCount() {
        return evictionCount;
    }

    public void setEvictionCount(long evictionCount) {
        this.evictionCount = evictionCount;
    }

    public long getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(long cacheSize) {
        this.cacheSize = cacheSize;
    }

    public double getAverageLoadPenalty() {
        return averageLoadPenalty;
    }

    public void setAverageLoadPenalty(double averageLoadPenalty) {
        this.averageLoadPenalty = averageLoadPenalty;
    }

    public long getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(long memorySize) {
        this.memorySize = memorySize;
    }

    /**
     * 计算命中率
     */
    public void calculateHitRate() {
        this.requestCount = this.hitCount + this.missCount;
        if (this.requestCount > 0) {
            this.hitRate = (double) this.hitCount / this.requestCount * 100;
        } else {
            this.hitRate = 0.0;
        }
    }
}
