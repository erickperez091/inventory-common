package com.example.common.utilities.impl;

import com.devskiller.friendly_id.FriendlyId;
import com.example.common.entity.EnumUtil;
import com.example.common.utilities.IdGeneratorService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ConditionalOnProperty(name = "common.id-generator", havingValue = "friendly_id", matchIfMissing = true)
public class FriendlyIdServiceImpl implements IdGeneratorService {

    @Override
    public String generateId(EnumUtil.UUIDType type ) {
        switch ( type ) {
            case LONG -> {
                return UUID.randomUUID().toString();
            }
            case SHORT -> {
                return FriendlyId.createFriendlyId();
            }
            default -> throw new IllegalArgumentException( "Not type was specified" );
        }
    }
}
