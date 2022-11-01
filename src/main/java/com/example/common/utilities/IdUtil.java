package com.example.common.utilities;

import com.devskiller.friendly_id.FriendlyId;
import com.example.common.entitty.EnumUtil;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdUtil {

    public String generateId( EnumUtil.UUIDType type ) {
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
