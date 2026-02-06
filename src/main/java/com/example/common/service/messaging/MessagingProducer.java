package com.example.common.service.messaging;

import com.example.common.entity.MessageEvent;

public interface MessagingProducer {

    void send(String destination, MessageEvent messageEvent);
}
