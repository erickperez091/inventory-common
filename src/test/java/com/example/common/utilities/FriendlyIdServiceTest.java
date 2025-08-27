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

public class FriendlyIdServiceTest {

    @Test
    void generateId_returnsUuidForLongType() {
        FriendlyIdServiceImpl friendlyIdService = new FriendlyIdServiceImpl();
        String id = friendlyIdService.generateId(EnumUtil.UUIDType.LONG);
        assertDoesNotThrow(() -> UUID.fromString(id));
    }

    @Test
    void generateId_returnsFriendlyIdForShortType() {
        FriendlyIdServiceImpl friendlyIdService = new FriendlyIdServiceImpl();
        try (MockedStatic<FriendlyId> friendlyIdMock = Mockito.mockStatic(FriendlyId.class)) {
            friendlyIdMock.when(FriendlyId::createFriendlyId).thenReturn("friendly-id");
            String id = friendlyIdService.generateId(EnumUtil.UUIDType.SHORT);
            assertEquals("friendly-id", id);
        }
    }
}
