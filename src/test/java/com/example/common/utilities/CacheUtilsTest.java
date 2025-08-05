package com.example.common.utilities;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CacheUtilsTest {

    @Test
    void getTtl_returnsCorrectTtl() {
        JwtUtils jwtUtils = Mockito.mock(JwtUtils.class);
        String token = "dummy-token";
        long now = System.currentTimeMillis();
        long expiration = now + 5000; // expires in 5 seconds

        Mockito.when(jwtUtils.getExpiration(token)).thenReturn(new Date(expiration));
        CacheUtils cacheUtils = new CacheUtils(jwtUtils);
        int ttl = cacheUtils.getTtl(token);

        // Accept TTL between 4 and 5 seconds due to timing
        assertTrue(ttl >= 4 && ttl <= 5);
    }
}
