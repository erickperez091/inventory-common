package com.example.common.utilities.impl;

import com.example.common.entity.EnumUtil;
import com.example.common.utilities.IdGeneratorService;
import com.github.f4b6a3.ulid.UlidCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "common.id-generator", havingValue = "ulid_id")
public class UlidIdServiceImpl implements IdGeneratorService {

    @Override
    public String generateId(EnumUtil.UUIDType type) {
        return switch (type) {
            case SHORT -> UlidCreator.getUlid().toString().toLowerCase();
            case LONG -> UlidCreator.getUlid().toRfc4122().toUuid().toString();
        };
    }
}
