package com.example.common.utilities;

import com.devskiller.friendly_id.FriendlyId;
import com.example.common.entity.EnumUtil;
import com.example.common.utilities.impl.FriendlyIdServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FriendlyIdServiceTest {

    @Test
    void generateId_returnsUuidForLongType() {
        IdGeneratorService idGeneratorService = new FriendlyIdServiceImpl();
        String id = idGeneratorService.generateId(EnumUtil.UUIDType.LONG);
        assertDoesNotThrow(() -> UUID.fromString(id));
    }

    @Test
    void generateId_returnsFriendlyIdForShortType() {
        IdGeneratorService idGeneratorService = new FriendlyIdServiceImpl();
        try (MockedStatic<FriendlyId> friendlyIdMock = Mockito.mockStatic(FriendlyId.class)) {
            friendlyIdMock.when(FriendlyId::createFriendlyId).thenReturn("friendly-id");
            String id = idGeneratorService.generateId(EnumUtil.UUIDType.SHORT);
            assertEquals("friendly-id", id);
        }
    }
}
