package com.example.common.entitty;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import com.example.common.entitty.EnumUtil.EventType;

import java.io.Serializable;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class MessageEvent implements Serializable {

    private EventType eventName;
    private Map<String, Object> payload;

    public EventType getEventName ( ) {
        return this.eventName;
    }

    public Map<String, Object> getPayload ( ) {
        return this.payload;
    }

    public void setEventName ( EventType eventName ) {
        this.eventName = eventName;
    }

    public void setPayload ( Map<String, Object> payload ) {
        this.payload = payload;
    }

    public String toString ( ) {
        return "MessageEvent(eventName=" + this.getEventName( ) + ", payload=" + this.getPayload( ) + ")";
    }
}