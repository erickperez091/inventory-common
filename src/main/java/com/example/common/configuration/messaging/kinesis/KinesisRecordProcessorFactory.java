package com.example.common.configuration.messaging.kinesis;

import com.example.common.service.messaging.MessagingCosumer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.kinesis.processor.ShardRecordProcessor;
import software.amazon.kinesis.processor.ShardRecordProcessorFactory;

@RequiredArgsConstructor
@Component
@ConditionalOnProperty(name = "messaging.provider", havingValue = "kinesis")
public class KinesisRecordProcessorFactory implements ShardRecordProcessorFactory {
    private final MessagingCosumer messagingConsumer;
    private final ObjectMapper objectMapper;

    @Override
    public ShardRecordProcessor shardRecordProcessor() {
        return new KinesisRecordProcessor(messagingConsumer, objectMapper);
    }
}