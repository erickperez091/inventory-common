package com.example.common.configuration;// src/test/java/com/example/common/configuration/CacheConfigTest.java
import com.example.common.configuration.cache.CacheConfig;
import com.example.common.service.cache.impl.MemcachedService;
import com.example.common.service.cache.impl.RedisService;
import com.example.common.utilities.CacheUtils;
import net.spy.memcached.MemcachedClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class CacheConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withBean(CacheUtils.class, () -> mock(CacheUtils.class))
            .withBean(StringRedisTemplate.class, () -> mock(StringRedisTemplate.class))
            .withUserConfiguration(CacheConfig.class);

    @Test
    void memcachedServiceBeanCreatedWhenMemcachedProvider() {
        contextRunner
                .withPropertyValues("cache.enabled=true", "cache.provider=memcached")
                .withBean(MemcachedClient.class, () -> mock(MemcachedClient.class))
                .run(context -> {
                    assertThat(context).hasSingleBean(MemcachedService.class);
                    assertThat(context).doesNotHaveBean(RedisService.class);
                });
    }

    @Test
    void redisServiceBeanCreatedWhenRedisProvider() {
        contextRunner
                .withPropertyValues("cache.enabled=true", "cache.provider=redis")
                .run(context -> {
                    assertThat(context).hasSingleBean(RedisService.class);
                    assertThat(context).doesNotHaveBean(MemcachedService.class);
                });
    }

    @Test
    void noCacheBeansWhenDisabled() {
        contextRunner
                .withPropertyValues("cache.enabled=false")
                .run(context -> {
                    assertThat(context).doesNotHaveBean(MemcachedService.class);
                    assertThat(context).doesNotHaveBean(RedisService.class);
                });
    }
}