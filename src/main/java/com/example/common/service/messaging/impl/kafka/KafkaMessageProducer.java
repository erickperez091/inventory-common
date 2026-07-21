package com.example.common.service.messaging.impl.kafka;

import com.example.common.aspect.AddCreatedBy;
import com.example.common.entity.MessageEvent;
import com.example.common.service.messaging.MessagingProducer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Log4j2
public class KafkaMessageProducer implements MessagingProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostConstruct
    public void init() {
        logger.info("[KafkaMessageProducer]: Creating Class Beans KafkaMessageProducer");
    }

    @Override
    public void send(String destination, MessageEvent messageEvent) {
        logger.info("Start sending message to [{}] topic, message: {}", destination, messageEvent);
        ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(destination, messageEvent);
        CompletableFuture<SendResult<String, Object>> response = kafkaTemplate.send(producerRecord);
        response.whenComplete((result, ex) -> {
            if (ex != null) {
                logger.error("Unable to send message=[{}] due to : {}", messageEvent, ex.getMessage());
            } else {
                logger.info("Sent message=[{}] with offset=[{}]", messageEvent, result.getRecordMetadata().offset());
            }
        });
    }
}
