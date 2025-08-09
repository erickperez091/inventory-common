package com.example.common.service.messaging;

import com.example.common.entity.MessageEvent;

public interface MessagingCosumer {

    void consume(MessageEvent messageEvent);
}
