package com.example.common.service.cache;

public interface CacheService {

    String NAMESPACE = "authorization:";
    String BLACKLIST_NAMESPACE = "blacklist:";
    String HEADER_UPDATE_LOGGING_ATTEMPTS = "header-update-logging-attempts:";
    int LOGIN_ATTEMPT_TTL = 60;

    void addTokenToBlackList(String token);

    boolean isTokenInBlackList(String token);

    String isSessionActive(String username);

    void storeActiveToken(String username, String token);

    String updateLoginAttempt(String username);

    String isLoginAttemptExpired(String idLoginAttempt);
}
