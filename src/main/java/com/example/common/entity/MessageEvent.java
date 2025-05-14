package com.example.common.entity;

import com.example.common.entity.EnumUtil.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageEvent implements Serializable {

    private EventType eventName;
    private Map< String, Object > payload;

}

