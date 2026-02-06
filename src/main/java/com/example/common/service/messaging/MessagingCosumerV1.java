package com.example.common.service.messaging;

import com.example.common.entity.MessageEvent;

public interface MessagingCosumerV1 {

    void consume(MessageEvent messageEvent);
}
