package com.example.common.configuration.messaging.kinesis;

import com.example.common.entity.MessageEvent;
import com.example.common.service.messaging.MessageConsumerRouter;
import com.example.common.service.messaging.MessagingConsumer;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.GetRecordsRequest;
import software.amazon.awssdk.services.kinesis.model.GetRecordsResponse;
import software.amazon.awssdk.services.kinesis.model.GetShardIteratorRequest;
import software.amazon.awssdk.services.kinesis.model.GetShardIteratorResponse;
import software.amazon.awssdk.services.kinesis.model.ListShardsRequest;
import software.amazon.awssdk.services.kinesis.model.ListShardsResponse;
import software.amazon.awssdk.services.kinesis.model.ShardIteratorType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Log4j2
public class KinesisMessageListener {

    private final KinesisClient kinesisClient;
    private final MessageConsumerRouter router;
    private final List<MessagingConsumer> consumers;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, String> shardIterators = new ConcurrentHashMap<>();


    @PostConstruct
    public void init() {
        consumers.stream()
                .map(MessagingConsumer::destination)
                .distinct()
                .forEach(this::initStream);
    }

    private void initStream(String destination) {
        logger.info("Initializing Kinesis listener for stream [{}]", destination);

        ListShardsResponse shardsResponse = kinesisClient.listShards(
                ListShardsRequest.builder()
                        .streamName(destination)
                        .build()
        );

        if (shardsResponse.shards().isEmpty()) {
            throw new IllegalStateException("No shards found for stream: " + destination);
        }

        String shardId = shardsResponse.shards().get(0).shardId();

        GetShardIteratorResponse iteratorResponse =
                kinesisClient.getShardIterator(
                        GetShardIteratorRequest.builder()
                                .streamName(destination)
                                .shardId(shardId)
                                .shardIteratorType(ShardIteratorType.LATEST)
                                .build()
                );

        shardIterators.put(destination, iteratorResponse.shardIterator());
    }

    @Scheduled(fixedDelay = 1000)
    public void pollStreams() {
        shardIterators.forEach(this::pollStream);
    }

    private void pollStream(String streamName, String iterator) {
        if (iterator == null) return;

        GetRecordsResponse response = kinesisClient.getRecords(
                GetRecordsRequest.builder()
                        .shardIterator(iterator)
                        .limit(25)
                        .build()
        );

        response.records().forEach(record -> {
            try {
                MessageEvent event = objectMapper.readValue(
                        record.data().asByteArray(),
                        MessageEvent.class
                );
                router.route(streamName, event);
            } catch (Exception e) {
                logger.error("Error processing record from stream [{}]", streamName, e);
            }
        });

        shardIterators.put(streamName, response.nextShardIterator());
    }
}
