package com.example.common.configuration.messaging.kinesis;

import com.example.common.entity.MessageEvent;
import com.example.common.service.messaging.MessagingCosumerV1;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import software.amazon.kinesis.exceptions.InvalidStateException;
import software.amazon.kinesis.exceptions.ShutdownException;
import software.amazon.kinesis.exceptions.ThrottlingException;
import software.amazon.kinesis.lifecycle.events.InitializationInput;
import software.amazon.kinesis.lifecycle.events.LeaseLostInput;
import software.amazon.kinesis.lifecycle.events.ProcessRecordsInput;
import software.amazon.kinesis.lifecycle.events.ShardEndedInput;
import software.amazon.kinesis.lifecycle.events.ShutdownRequestedInput;
import software.amazon.kinesis.processor.ShardRecordProcessor;
import software.amazon.kinesis.retrieval.KinesisClientRecord;

import java.nio.charset.StandardCharsets;

@ConditionalOnProperty(name = "messaging.provider", havingValue = "kinesis")
@RequiredArgsConstructor
@Log4j2
public class KinesisRecordProcessor implements ShardRecordProcessor {
    private final MessagingCosumerV1 messagingConsumer;
    private final ObjectMapper objectMapper;

    @Override
    public void initialize(InitializationInput initializationInput) {

    }

    @Override
    public void processRecords(ProcessRecordsInput input) {
        if (input.records().isEmpty()) {
            return;
        }
        for (KinesisClientRecord rec : input.records()) {
            try {
                String json = StandardCharsets.UTF_8.decode(rec.data()).toString();
                MessageEvent evt = objectMapper.readValue(json, MessageEvent.class);
                messagingConsumer.consume(evt);
            } catch (Exception e) {
                logger.error("Error deserializing/consuming record", e);
            }
        }
        try {
            input.checkpointer().checkpoint();
            logger.debug("Checkpoint done");
        } catch (ShutdownException | ThrottlingException | InvalidStateException e) {
            logger.warn("Checkpoint batch couldn't be done. No se pudo hacer checkpoint del batch", e);
        }
    }


    @Override
    public void leaseLost(LeaseLostInput leaseLostInput) {

    }

    @Override
    public void shardEnded(ShardEndedInput shardEndedInput) {
        try {
            shardEndedInput.checkpointer().checkpoint();
            logger.info("Shard ended - checkpoint final realizado");
        } catch (ShutdownException | InvalidStateException e) {
            logger.warn("No se pudo checkpoint en shardEnded", e);
        }
    }

    @Override
    public void shutdownRequested(ShutdownRequestedInput shutdownRequestedInput) {
        try {
            shutdownRequestedInput.checkpointer().checkpoint();
            logger.info("Shutdown solicitado - checkpoint realizado");
        } catch (ShutdownException | InvalidStateException e) {
            logger.warn("No se pudo checkpoint en shutdownRequested", e);
        }
    }
}
