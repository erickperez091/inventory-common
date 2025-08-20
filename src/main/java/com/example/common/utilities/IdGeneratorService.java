package com.example.common.utilities;

import com.example.common.entity.EnumUtil;

public interface IdGeneratorService {
    String generateId(EnumUtil.UUIDType type);
}
