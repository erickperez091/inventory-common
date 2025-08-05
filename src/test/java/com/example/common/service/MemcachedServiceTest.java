package com.example.common.service;// src/test/java/com/example/common/service/impl/MemcachedServiceTest.java
import com.example.common.service.impl.MemcachedService;
import com.example.common.utilities.CacheUtils;
import net.spy.memcached.MemcachedClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class MemcachedServiceTest {

    private MemcachedClient memcachedClient;
    private CacheUtils cacheUtils;
    private MemcachedService memcachedService;

    @BeforeEach
    void setUp() {
        memcachedClient = mock(MemcachedClient.class);
        cacheUtils = mock(CacheUtils.class);
        memcachedService = new MemcachedService(memcachedClient, cacheUtils);
    }

    @Test
    void addTokenToBlackList_savesTokenIfTtlPositive() {
        String token = "abc";
        when(cacheUtils.getTtl(token)).thenReturn(100);

        memcachedService.addTokenToBlackList(token);

        verify(memcachedClient).add(contains(token), eq(100), eq("true"));
    }

    @Test
    void addTokenToBlackList_doesNothingIfTtlZero() {
        String token = "abc";
        when(cacheUtils.getTtl(token)).thenReturn(0);

        memcachedService.addTokenToBlackList(token);

        verify(memcachedClient, never()).add(anyString(), anyInt(), any());
    }

    @Test
    void isTokenInBlackList_returnsTrueIfExists() {
        String token = "abc";
        when(memcachedClient.get(anyString())).thenReturn("true");

        assertTrue(memcachedService.isTokenInBlackList(token));
    }

    @Test
    void isTokenInBlackList_returnsFalseIfNotExists() {
        String token = "abc";
        when(memcachedClient.get(anyString())).thenReturn(null);

        assertFalse(memcachedService.isTokenInBlackList(token));
    }

    @Test
    void isSessionActive_returnsTokenIfExists() {
        String username = "user";
        when(memcachedClient.get(anyString())).thenReturn("token123");

        assertEquals("token123", memcachedService.isSessionActive(username));
    }

    @Test
    void storeActiveToken_savesTokenIfTtlPositive() {
        String username = "user";
        String token = "token123";
        when(cacheUtils.getTtl(token)).thenReturn(50);

        memcachedService.storeActiveToken(username, token);

        verify(memcachedClient).add(contains(username), eq(50), eq(token));
    }

    @Test
    void storeActiveToken_doesNothingIfTtlZero() {
        String username = "user";
        String token = "token123";
        when(cacheUtils.getTtl(token)).thenReturn(0);

        memcachedService.storeActiveToken(username, token);

        verify(memcachedClient, never()).add(anyString(), anyInt(), any());
    }
}