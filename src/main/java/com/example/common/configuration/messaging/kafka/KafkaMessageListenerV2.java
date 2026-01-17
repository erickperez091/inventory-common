package com.example.common.configuration.messaging.kafka;

import com.example.common.entity.MessageEvent;
import com.example.common.service.messaging.MessagingCosumerV1;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor
public class KafkaMessageListenerV2 {
    private final MessagingCosumerV1 messagingConsumer;
    private final String topic;
    private final String groupId;

    @KafkaListener(topics = "#{__listener.topic}", groupId = "#{__listener.groupId}")
    public void consume(MessageEvent messageEvent){
        this.messagingConsumer.consume(messageEvent);
    }

}