package com.example.common.service.messaging.impl.kinesis;

import com.example.common.aspect.AddCreatedBy;
import com.example.common.entity.MessageEvent;
import com.example.common.service.messaging.MessagingProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;

import java.nio.ByteBuffer;

@RequiredArgsConstructor
@Log4j2
public class KinesisAsyncMessageProducer implements MessagingProducer {

    private final KinesisAsyncClient kinesisAsyncClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        logger.info("[KinesisAsyncMessageProducer]: Creating Class Beans KinesisAsyncMessageProducer");
    }

    @Override
    public void send(String destination, MessageEvent messageEvent) {
        try {
            byte[] jsonBytes = objectMapper.writeValueAsBytes(messageEvent);

            PutRecordRequest request = PutRecordRequest.builder()
                    .streamName(destination)
                    .partitionKey(messageEvent.getEventName().name())
                    .data(SdkBytes.fromByteBuffer(ByteBuffer.wrap(jsonBytes)))
                    .build();

            kinesisAsyncClient.putRecord(request)
                    .thenAccept(response -> {
                        logger.info("Message Sent: {} to Shard: {}", messageEvent, response.shardId());
                    })
                    .exceptionally(ex -> {
                        logger.error("Fail sending message to Kinesis: {}", ex.getMessage());
                        return null;
                    });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing message to JSON:", e);
        }
    }
}
