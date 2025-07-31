package com.example.common.service;

import com.example.common.utilities.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Log4j2
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;
    private final JwtUtils jwtUtils;
    private final static String NAMESPACE = "authorization:";

    public void addTokenToBlackList(String token) {
        logger.info("[RedisBlackListService][addTokenToBlackList][Start]: Start Calculating remaining time for token");
        long ttl = (this.jwtUtils.getExpiration(token).getTime() - System.currentTimeMillis()) / 1000;
        logger.info("[RedisBlackListService][addTokenToBlackList][Start]: End Calculating remaining time for token");
        if (ttl > 0) {
            this.saveValueForRedis("blacklist:" + token, "true", ttl);
        }
    }

    public boolean isTokenInBlackList(String token) {
        try {
            logger.info("[RedisBlackListService][isTokenInBlackList][Start]: Start Validating token into blacklist");
            String lookup = String.format("%s%s%s", NAMESPACE, "blacklist:", token);
            return this.lookupValueRedis(lookup) != null;
        } finally {
            logger.info("[RedisBlackListService][isTokenInBlackList][End]:  End Validating token into blacklist");
        }
    }

    public String isSessionActive(String username) {
        try {
            logger.info("[RedisBlackListService][isSessionActive][Start]: Start Validating session active");
            String lookup = String.format("%s%s%s", NAMESPACE, "active:", username);
            return this.lookupValueRedis(lookup);

        } finally {
            logger.info("[RedisBlackListService][isSessionActive][End]:  End Validating session active");
        }
    }

    public void storeActiveToken(String username, String token) {
        long ttl = (this.jwtUtils.getExpiration(token).getTime() - System.currentTimeMillis()) / 1000;
        if (ttl > 0) {
            this.saveValueForRedis("active:" + username, token, ttl);
        }
    }

    private void saveValueForRedis(String key, String value, long ttl) {
        logger.info("[RedisBlackListService][saveValueForRedis][Start]: Start Saving data to Redis Database");
        this.stringRedisTemplate.opsForValue().set(NAMESPACE + key, value, ttl, TimeUnit.SECONDS);
        logger.info("[RedisBlackListService][saveValueForRedis][Start]: End Saving data to Redis Database");
    }

    private String lookupValueRedis(String valueToLookup) {
        try {
            logger.info("[RedisBlackListService][lookupValueRedis][Start]: Start Looking for Redis Value");
            return this.stringRedisTemplate.opsForValue().get(valueToLookup);
        } finally {
            logger.info("[RedisBlackListService][lookupValueRedis][Start]: End Looking for Redis Value");
        }
    }
}
