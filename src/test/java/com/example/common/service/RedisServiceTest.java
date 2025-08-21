package com.example.common.service;

import com.example.common.service.cache.impl.RedisService;
import com.example.common.utilities.CacheUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RedisServiceTest {

    private CacheUtils cacheUtils;
    private RedisService redisService;
    private ValueOperations<String, String> valueOps;

    @BeforeEach
    void setUp() {
        StringRedisTemplate redisTemplate = mock(StringRedisTemplate.class);
        cacheUtils = mock(CacheUtils.class);
        valueOps = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        redisService = new RedisService(redisTemplate, cacheUtils);
    }

    @Test
    void addTokenToBlackList_savesTokenIfTtlPositive() {
        String token = "abc";
        when(cacheUtils.getTtl(token)).thenReturn(100);
        redisService.addTokenToBlackList(token);
        verify(valueOps).set(contains(token), eq("true"), eq(100L), eq(java.util.concurrent.TimeUnit.SECONDS));
    }

    @Test
    void addTokenToBlackList_doesNothingIfTtlZero() {
        String token = "abc";
        when(cacheUtils.getTtl(token)).thenReturn(0);

        redisService.addTokenToBlackList(token);

        verify(valueOps, never()).set(anyString(), anyString(), anyLong(), any());
    }

    @Test
    void isTokenInBlackList_returnsTrueIfExists() {
        String token = "abc";
        when(valueOps.get(anyString())).thenReturn("true");

        assertTrue(redisService.isTokenInBlackList(token));
    }

    @Test
    void isTokenInBlackList_returnsFalseIfNotExists() {
        String token = "abc";
        when(valueOps.get(anyString())).thenReturn(null);

        assertFalse(redisService.isTokenInBlackList(token));
    }

    @Test
    void isSessionActive_returnsTokenIfExists() {
        String username = "user";
        when(valueOps.get(anyString())).thenReturn("token123");

        assertEquals("token123", redisService.isSessionActive(username));
    }

    @Test
    void storeActiveToken_savesTokenIfTtlPositive() {
        String username = "user";
        String token = "token123";
        when(cacheUtils.getTtl(token)).thenReturn(50);
        redisService.storeActiveToken(username, token);
        verify(valueOps).set(contains(username), eq(token), eq(50L), eq(java.util.concurrent.TimeUnit.SECONDS));
    }

    @Test
    void storeActiveToken_doesNothingIfTtlZero() {
        String username = "user";
        String token = "token123";
        when(cacheUtils.getTtl(token)).thenReturn(0);
        redisService.storeActiveToken(username, token);
        verify(valueOps, never()).set(anyString(), anyString(), anyLong(), any());
    }
}