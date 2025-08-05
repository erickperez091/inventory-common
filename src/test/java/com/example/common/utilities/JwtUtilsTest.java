package com.example.common.utilities;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtUtilsTest {
    private static final String SECRET = "0123456789abcdef0123456789abcdef"; // 32 bytes for HS256
    private static final long EXPIRATION = 1000 * 60; // 1 minute

    @Test
    void generateToken_and_parseClaims() {
        JwtUtils jwtUtils = new JwtUtils(SECRET, EXPIRATION);
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "ADMIN");
        String token = jwtUtils.generateToken("user1", claims);

        assertNotNull(token);
        assertEquals("user1", jwtUtils.getUsername(token));
        assertEquals("ADMIN", jwtUtils.getRole(token));
        assertFalse(jwtUtils.isTokenExpired(token));
        assertTrue(jwtUtils.isTokenValid(token));
        assertNotNull(jwtUtils.getExpiration(token));
    }

    @Test
    void getClaim_returnsCorrectValue() {
        JwtUtils jwtUtils = new JwtUtils(SECRET, EXPIRATION);
        Map<String, Object> claims = new HashMap<>();
        claims.put("custom", "value");
        String token = jwtUtils.generateToken("user2", claims);

        String customClaim = jwtUtils.getClaim(token, "custom", String.class);
        assertEquals("value", customClaim);

        String subject = jwtUtils.getClaim(token, Claims::getSubject);
        assertEquals("user2", subject);
    }

    @Test
    void isTokenExpired_returnsTrueForExpiredToken() throws InterruptedException {
        JwtUtils jwtUtils = new JwtUtils(SECRET, 100); // 100 ms
        String token = jwtUtils.generateToken("user3", Map.of());

        Thread.sleep(150); // Wait for expiration
        assertTrue(jwtUtils.isTokenExpired(token));
        assertFalse(jwtUtils.isTokenValid(token));
    }
}
