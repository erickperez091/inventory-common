package com.example.common.configuration.messaging.kinesis;

import com.example.common.entity.MessageEvent;
import com.example.common.service.messaging.MessagingCosumerV1;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.GetRecordsRequest;
import software.amazon.awssdk.services.kinesis.model.GetRecordsResponse;
import software.amazon.awssdk.services.kinesis.model.GetShardIteratorRequest;
import software.amazon.awssdk.services.kinesis.model.GetShardIteratorResponse;
import software.amazon.awssdk.services.kinesis.model.ListShardsRequest;
import software.amazon.awssdk.services.kinesis.model.ListShardsResponse;
import software.amazon.awssdk.services.kinesis.model.ShardIteratorType;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
public class KinesisMessageListenerV1 {

    private final KinesisClient kinesisClient;
    private final MessagingCosumerV1 messagingCosumerV1;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String streamName;
    private String shardIterator;

    @PostConstruct
    public void init() {

        ListShardsResponse shardsResponse = kinesisClient.listShards(ListShardsRequest.builder()
                .streamName(streamName)
                .build());

        if (shardsResponse.shards().isEmpty()) {
            throw new IllegalStateException("No shards found for stream: " + streamName);
        }

        String shardId = shardsResponse.shards().get(0).shardId();

        GetShardIteratorRequest iteratorRequest = GetShardIteratorRequest.builder()
                .streamName(streamName)
                .shardId(shardId)
                .shardIteratorType(ShardIteratorType.LATEST)
                .build();

        GetShardIteratorResponse iteratorResponse = kinesisClient.getShardIterator(iteratorRequest);
        shardIterator = iteratorResponse.shardIterator();
    }

    @Scheduled(fixedDelay = 1000)
    public void pollStream() {
        if (shardIterator == null) return;

        GetRecordsRequest recordsRequest = GetRecordsRequest.builder()
                .shardIterator(shardIterator)
                .limit(25)
                .build();

        GetRecordsResponse response = kinesisClient.getRecords(recordsRequest);
        List<software.amazon.awssdk.services.kinesis.model.Record> records = response.records();

        for (software.amazon.awssdk.services.kinesis.model.Record record : records) {
            try {
                SdkBytes data = record.data();
                String json = data.asString(StandardCharsets.UTF_8);
                MessageEvent event = objectMapper.readValue(json, MessageEvent.class);
                messagingCosumerV1.consume(event);
            } catch (Exception e) {
                System.err.println("Error deserializing Kinesis message: " + e.getMessage());
            }
        }

        shardIterator = response.nextShardIterator();
    }
}
