package com.example.common.utilities.impl;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.example.common.entity.EnumUtil;
import com.example.common.utilities.IdGeneratorService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.UUID;

@Service
@ConditionalOnProperty(name = "common.id-generator", havingValue = "nano_id")
public class NanoIdServiceImpl implements IdGeneratorService {

    public static final char[] CUSTOM_ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    @Override
    public String generateId(EnumUtil.UUIDType type) {
        return switch (type) {
            case SHORT -> NanoIdUtils.randomNanoId(new SecureRandom(), CUSTOM_ALPHABET, 21);
            case LONG -> UUID.randomUUID().toString();
        };
    }
}
