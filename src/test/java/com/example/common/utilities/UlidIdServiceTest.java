package com.example.common.utilities;

import com.example.common.entity.EnumUtil;
import com.example.common.utilities.impl.UlidIdServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UlidIdServiceTest {

    private final static String ULID_PATTERN = "^[0-7][0-9A-HJKMNP-TV-Za-hjkmnp-tv-z]{25}$";
    private final static String UUID_PATTERN = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

    @Test
    void generateUlidShortTest() {
        IdGeneratorService idGeneratorService = new UlidIdServiceImpl();
        String id = idGeneratorService.generateId(EnumUtil.UUIDType.SHORT);
        assertTrue(id.matches(ULID_PATTERN));
        assertEquals(26, id.length());
    }

    @Test
    void generateUlidLongTest() {
        IdGeneratorService idGeneratorService = new UlidIdServiceImpl();
        String id = idGeneratorService.generateId(EnumUtil.UUIDType.LONG);
        assertTrue(id.matches(UUID_PATTERN));
        assertTrue(id.length() > 26);
    }
}
