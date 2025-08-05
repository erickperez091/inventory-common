package com.example.common.service.impl;

import com.example.common.service.CacheService;
import com.example.common.utilities.CacheUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.spy.memcached.MemcachedClient;

import java.util.Objects;

import static com.example.common.utilities.CacheUtils.BLACKLIST_NAMESPACE;
import static com.example.common.utilities.CacheUtils.NAMESPACE;


@RequiredArgsConstructor
@Log4j2
public class MemcachedService implements CacheService {

    public final MemcachedClient memcachedClient;
    private final CacheUtils cacheUtils;

    @Override
    public void addTokenToBlackList(String token) {
        int ttl = this.cacheUtils.getTtl(token);
        if (ttl > 0) {
            this.saveValueForMemcached(BLACKLIST_NAMESPACE + token, "true", ttl);
        }
    }

    @Override
    public boolean isTokenInBlackList(String token) {
        try {
            logger.info("[MemcachedService][isTokenInBlackList][Start]: Start Validating token into blacklist");
            String lookup = String.format("%s%s%s", NAMESPACE, BLACKLIST_NAMESPACE, token);
            return this.lookupValueMemcached(lookup) != null;
        } finally {
            logger.info("[MemcachedService][isTokenInBlackList][End]:  End Validating token into blacklist");
        }
    }

    @Override
    public String isSessionActive(String username) {
        try {
            logger.info("[MemcachedService][isSessionActive][Start]: Validating session active");
            String lookup = String.format("%s%s%s", NAMESPACE, "active:", username);
            return this.lookupValueMemcached(lookup);
        } finally {
            logger.info("[MemcachedService][isSessionActive][End]: Validating session active");
        }
    }

    @Override
    public void storeActiveToken(String username, String token) {
        int ttl = this.cacheUtils.getTtl(token);
        if (ttl > 0) {
            this.saveValueForMemcached("active:" + username, token, ttl);
        }
    }

    private void saveValueForMemcached(String key, String value, int ttl) {
        logger.info("[MemcachedService][saveValueForMemcached][Start]: Saving data to Redis Database");
        this.memcachedClient.add(NAMESPACE + key, ttl, value);
        logger.info("[MemcachedService][saveValueForMemcached][End]: Saving data to Redis Database");
    }

    private String lookupValueMemcached(String valueToLookup) {
        try {
            logger.info("[MemcachedService][lookupValueMemcached][Start]: Looking for Redis Value");
            return Objects.isNull(this.memcachedClient.get(valueToLookup)) ? null : this.memcachedClient.get(valueToLookup).toString();
        } finally {
            logger.info("[MemcachedService][lookupValueMemcached][End]: Looking for Redis Value");
        }
    }
}
