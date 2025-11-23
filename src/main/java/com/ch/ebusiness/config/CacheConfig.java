package com.ch.ebusiness.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 多级缓存配置类
 * 配置Caffeine本地缓存和Redis分布式缓存
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${cache.caffeine.max-size:1000}")
    private long caffeineMaxSize;

    @Value("${cache.caffeine.expire-after-write:60}")
    private long caffeineExpireAfterWrite;

    @Value("${cache.ttl.goods-types:86400}")
    private long goodsTypesTtl;

    @Value("${cache.ttl.lasted-goods:3600}")
    private long lastedGoodsTtl;

    @Value("${cache.ttl.goods-detail:7200}")
    private long goodsDetailTtl;

    @Value("${cache.ttl.search-result:1800}")
    private long searchResultTtl;

    /**
     * 配置Caffeine本地缓存管理器（一级缓存）
     */
    @Bean("caffeineCacheManager")
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(caffeineMaxSize)
                .expireAfterWrite(caffeineExpireAfterWrite, TimeUnit.MINUTES)
                .recordStats()); // 开启统计功能
        return cacheManager;
    }

    /**
     * 配置Redis缓存管理器（二级缓存）
     */
    @Bean("redisCacheManager")
    @Primary
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        // 配置序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
                Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        // 默认缓存配置
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(jackson2JsonRedisSerializer))
                .disableCachingNullValues()
                .entryTtl(Duration.ofHours(1)); // 默认1小时

        // 针对不同缓存空间设置不同的过期时间
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        // 商品类型缓存 - 24小时
        cacheConfigurations.put("goodsTypes", defaultConfig
                .entryTtl(Duration.ofSeconds(goodsTypesTtl)));

        // 首页商品列表缓存 - 1小时
        cacheConfigurations.put("lastedGoods", defaultConfig
                .entryTtl(Duration.ofSeconds(lastedGoodsTtl)));

        // 商品详情缓存 - 2小时
        cacheConfigurations.put("goodsDetail", defaultConfig
                .entryTtl(Duration.ofSeconds(goodsDetailTtl)));

        // 搜索结果缓存 - 30分钟
        cacheConfigurations.put("searchResult", defaultConfig
                .entryTtl(Duration.ofSeconds(searchResultTtl)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .transactionAware()
                .build();
    }

    /**
     * 配置RedisTemplate用于直接操作Redis
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
                Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }
}
