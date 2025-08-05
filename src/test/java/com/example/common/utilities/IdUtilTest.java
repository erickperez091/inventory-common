package com.example.common.utilities;

import com.devskiller.friendly_id.FriendlyId;
import com.example.common.entity.EnumUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IdUtilTest {

    @Test
    void generateId_returnsUuidForLongType() {
        IdUtil idUtil = new IdUtil();
        String id = idUtil.generateId(EnumUtil.UUIDType.LONG);
        assertDoesNotThrow(() -> UUID.fromString(id));
    }

    @Test
    void generateId_returnsFriendlyIdForShortType() {
        IdUtil idUtil = new IdUtil();
        try (MockedStatic<FriendlyId> friendlyIdMock = Mockito.mockStatic(FriendlyId.class)) {
            friendlyIdMock.when(FriendlyId::createFriendlyId).thenReturn("friendly-id");
            String id = idUtil.generateId(EnumUtil.UUIDType.SHORT);
            assertEquals("friendly-id", id);
        }
    }
}
