package com.ch.ebusiness.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁工具类
 * 用于处理高并发场景下的资源竞争问题
 */
@Component
public class RedisLockUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 尝试获取分布式锁
     * 
     * @param lockKey    锁的key
     * @param requestId  请求标识（用于释放锁时验证）
     * @param expireTime 锁的过期时间（秒）
     * @return 是否成功获取锁
     */
    public boolean tryLock(String lockKey, String requestId, long expireTime) {
        try {
            Boolean result = redisTemplate.opsForValue().setIfAbsent(
                    lockKey,
                    requestId,
                    expireTime,
                    TimeUnit.SECONDS);
            return result != null && result;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 释放分布式锁
     * 
     * @param lockKey   锁的key
     * @param requestId 请求标识（必须与获取锁时的标识一致）
     * @return 是否成功释放锁
     */
    public boolean releaseLock(String lockKey, String requestId) {
        try {
            Object value = redisTemplate.opsForValue().get(lockKey);
            if (requestId.equals(value)) {
                return Boolean.TRUE.equals(redisTemplate.delete(lockKey));
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 生成唯一请求ID
     */
    public static String generateRequestId() {
        return UUID.randomUUID().toString();
    }
}
