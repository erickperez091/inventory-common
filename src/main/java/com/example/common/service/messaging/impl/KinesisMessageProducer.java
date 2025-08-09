package com.example.common.service.messaging.impl;

import com.example.common.entity.MessageEvent;
import com.example.common.service.messaging.MessagingProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@RequiredArgsConstructor
@Log4j2
public class KinesisMessageProducer implements MessagingProducer {

    private final KinesisClient kinesisClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Executor executor;

    @Value("${messaging.kinesis.stream-name}")
    private String streamName;

    @PostConstruct
    public void init() {
        logger.info("[KinesisMessageProducer]: Creating Class Beans KinesisMessageProducer");
    }

    @Override
    public void send(MessageEvent messageEvent) {

        //Sync way

        /*try {
            byte[] jsonBytes = objectMapper.writeValueAsBytes(messageEvent);
            PutRecordRequest request = PutRecordRequest.builder()
                    .streamName(streamName)
                    .partitionKey(messageEvent.getEventName().name())
                    .data(SdkBytes.fromByteBuffer(ByteBuffer.wrap(jsonBytes)))
                    .build();

            kinesisClient.putRecord(request);
        } catch (Exception e) {
            throw new RuntimeException("Error sending message to Kinesis", e);
        }*/

        // Async way using completable future
        CompletableFuture.supplyAsync(() -> {
            try {
                byte[] jsonBytes = objectMapper.writeValueAsBytes(messageEvent);
                PutRecordRequest request = PutRecordRequest.builder()
                        .streamName(streamName)
                        .partitionKey(messageEvent.getEventName().name())
                        .data(SdkBytes.fromByteBuffer(ByteBuffer.wrap(jsonBytes)))
                        .build();

                return kinesisClient.putRecord(request);

            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error serializing message", e);
            }

        }, executor).whenComplete((response, throwable) -> {
            if (throwable != null) {
                logger.error("Error sending the message to kinesis", throwable);
            } else {
                logger.info("Message Sent: {} to Shard: {} with Sequence Number: {}",
                        messageEvent, response.shardId(), response.sequenceNumber()
                );
            }
        });
    }
}