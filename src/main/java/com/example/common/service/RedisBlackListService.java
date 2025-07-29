package com.example.common.service;

import com.example.common.utilities.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Log4j2
public class RedisBlackListService {

    private final StringRedisTemplate stringRedisTemplate;
    private final JwtUtils jwtUtils;

    public void addTokenToBlackList(String token) {
        logger.info("[RedisBlackListService][addTokenToBlackList][Start]: Start Calculating remaining time for token");
        long ttl = (this.jwtUtils.getExpiration(token).getTime() - System.currentTimeMillis()) / 1000;
        logger.info("[RedisBlackListService][addTokenToBlackList][Start]: End Calculating remaining time for token");
        if (ttl > 0) {
            logger.info("[RedisBlackListService][addTokenToBlackList][Start]: Start Saving token to a blacklist");
            this.stringRedisTemplate.opsForValue().set("blacklist:" + token, "true", ttl, TimeUnit.SECONDS);
            logger.info("[RedisBlackListService][addTokenToBlackList][Start]: End Saving token to a blacklist");
        }
    }

    public boolean isTokenInBlackList(String token) {
        try {
            logger.info("[RedisBlackListService][isTokenInBlackList][Start]: Start Validating token into blacklist");
            return this.stringRedisTemplate.opsForValue().get("blacklist:" + token) != null;
        } finally {
            logger.info("[RedisBlackListService][isTokenInBlackList][End]:  End Validating token into blacklist");
        }
    }

}
