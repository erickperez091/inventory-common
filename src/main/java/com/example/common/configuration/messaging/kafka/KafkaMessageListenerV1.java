package com.example.common.configuration.messaging.kafka;

import com.example.common.entity.MessageEvent;
import com.example.common.service.messaging.MessagingCosumerV1;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor
public class KafkaMessageListenerV1 {

    private final MessagingCosumerV1 messagingCosumerV1;

    @KafkaListener(topics = "${messaging.kafka.topic}", groupId = "${messaging.kafka.group-id}")
    public void consume(MessageEvent messageEvent) {
        this.messagingCosumerV1.consume(messageEvent);
    }
}
