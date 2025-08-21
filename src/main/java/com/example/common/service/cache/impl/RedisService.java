package com.example.common.service.cache.impl;

import com.example.common.service.cache.CacheService;
import com.example.common.utilities.CacheUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

import static com.example.common.utilities.CacheUtils.BLACKLIST_NAMESPACE;
import static com.example.common.utilities.CacheUtils.NAMESPACE;

@RequiredArgsConstructor
@Log4j2
public class RedisService implements CacheService {

    private final StringRedisTemplate stringRedisTemplate;
    private final CacheUtils cacheUtils;

    @Override
    public void addTokenToBlackList(String token) {
        long ttl = this.cacheUtils.getTtl(token);
        if (ttl > 0) {
            this.saveValueForRedis(BLACKLIST_NAMESPACE + token, "true", ttl);
        }
    }

    @Override
    public boolean isTokenInBlackList(String token) {
        try {
            logger.info("[RedisService][isTokenInBlackList][Start]: Validating token into blacklist");
            String lookup = String.format("%s%s%s", NAMESPACE, BLACKLIST_NAMESPACE, token);
            return this.lookupValueRedis(lookup) != null;
        } finally {
            logger.info("[RedisService][isTokenInBlackList][End]: Validating token into blacklist");
        }
    }

    @Override
    public String isSessionActive(String username) {
        try {
            logger.info("[RedisService][isSessionActive][Start]: Validating session active");
            String lookup = String.format("%s%s%s", NAMESPACE, "active:", username);
            return this.lookupValueRedis(lookup);
        } finally {
            logger.info("[RedisService][isSessionActive][End]: Validating session active");
        }
    }

    @Override
    public void storeActiveToken(String username, String token) {
        long ttl = this.cacheUtils.getTtl(token);
        if (ttl > 0) {
            this.saveValueForRedis("active:" + username, token, ttl);
        }
    }

    private void saveValueForRedis(String key, String value, long ttl) {
        logger.info("[RedisService][saveValueForRedis][Start]: Saving data to Redis Database");
        this.stringRedisTemplate.opsForValue().set(NAMESPACE + key, value, ttl, TimeUnit.SECONDS);
        logger.info("[RedisService][saveValueForRedis][End]: Saving data to Redis Database");
    }

    private String lookupValueRedis(String valueToLookup) {
        try {
            logger.info("[RedisService][lookupValueRedis][Start]: Looking for Redis Value");
            return this.stringRedisTemplate.opsForValue().get(valueToLookup);
        } finally {
            logger.info("[RedisService][lookupValueRedis][End]: Looking for Redis Value");
        }
    }
}
