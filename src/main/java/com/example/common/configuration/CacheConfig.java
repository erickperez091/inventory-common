package com.example.common.configuration;

import com.example.common.service.impl.MemcachedService;
import com.example.common.service.impl.RedisService;
import com.example.common.utilities.CacheUtils;
import lombok.RequiredArgsConstructor;
import net.spy.memcached.MemcachedClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "cache.enabled", havingValue = "true")
public class CacheConfig {

    private final CacheUtils cacheUtils;
    private final StringRedisTemplate stringRedisTemplate;

    @Bean
    @ConditionalOnProperty(name = "cache.provider", havingValue = "memcached")
    public MemcachedService memcachedService(MemcachedClient memcachedClient) {
        return new MemcachedService(memcachedClient, cacheUtils);
    }

    @Bean
    @ConditionalOnProperty(name = "cache.provider", havingValue = "redis")
    public RedisService redisService() {
        return new RedisService(stringRedisTemplate, cacheUtils);
    }
}
