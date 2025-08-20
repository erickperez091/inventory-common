package com.example.common.configuration.messaging.kafka;

import com.example.common.entity.MessageEvent;
import com.example.common.service.messaging.MessagingCosumer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor
public class KafkaMessageListener {

    private final MessagingCosumer messagingCosumer;

    @KafkaListener(topics = "${messaging.kafka.topic}", groupId = "${messaging.kafka.group-id}")
    public void consume(MessageEvent messageEvent) {
        this.messagingCosumer.consume(messageEvent);
    }
}
