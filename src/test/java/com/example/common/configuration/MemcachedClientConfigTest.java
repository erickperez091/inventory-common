package com.example.common.configuration;

import net.spy.memcached.MemcachedClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class MemcachedClientConfigTest {

    @TestConfiguration
    static class MockMemcachedConfig {
        @Bean
        public MemcachedClient memcachedClient() {
            return mock(MemcachedClient.class);
        }
    }

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withPropertyValues("cache.provider=memcached", "memcached.host=testhost", "memcached.port=12345")
            .withUserConfiguration(MockMemcachedConfig.class);

    @Test
    void memcachedClientBeanCreatedWhenMemcachedProvider() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(MemcachedClient.class);
        });
    }
}
