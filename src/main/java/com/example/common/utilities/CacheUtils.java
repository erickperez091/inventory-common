package com.example.common.utilities;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@Component
public class CacheUtils {

    public final static String NAMESPACE = "authorization:";
    public final static String BLACKLIST_NAMESPACE = "blacklist:";

    private final JwtUtils jwtUtils;

    public int getTtl(String token) {
        logger.info("[CacheUtils][getTtl][Start]: Calculating remaining time for token");
        int ttl = Math.toIntExact((this.jwtUtils.getExpiration(token).getTime() - System.currentTimeMillis()) / 1000);
        logger.info("[CacheUtils][getTtl][End]: Calculating remaining time for token");
        return ttl;
    }
}
