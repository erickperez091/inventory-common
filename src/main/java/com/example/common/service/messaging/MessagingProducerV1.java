package com.example.common.service.messaging;

import com.example.common.entity.MessageEvent;

public interface MessagingProducerV1 {

    void send(MessageEvent messageEvent);
}
