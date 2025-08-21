package com.example.common.service.cache;

public interface CacheService {

    void addTokenToBlackList(String token);

    boolean isTokenInBlackList(String token);

    String isSessionActive(String username);

    void storeActiveToken(String username, String token);
}
