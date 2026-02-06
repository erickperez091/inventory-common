package com.example.common.configuration.messaging.kafka;

import com.example.common.entity.MessageEvent;
import com.example.common.service.messaging.MessageConsumerRouter;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

@RequiredArgsConstructor
public class KafkaMessageListener {

    private final MessageConsumerRouter messageConsumerRouter;

    @KafkaListener(
            topics = "#{@kafkaTopics}",
            groupId = "${messaging.kafka.group-id}"
    )

    public void consume(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, MessageEvent event) {
        messageConsumerRouter.route(topic, event);
    }
}
