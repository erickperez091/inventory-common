package com.example.common.service.messaging;

import com.example.common.entity.MessageEvent;

public interface MessagingConsumer {

    String destination();

    void consume(MessageEvent messageEvent);
}
