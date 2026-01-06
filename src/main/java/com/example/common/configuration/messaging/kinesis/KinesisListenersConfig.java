package com.example.common.configuration.messaging.kinesis;

import com.example.common.service.messaging.MessagingCosumer;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.kinesis.KinesisClient;

import java.util.List;

@RequiredArgsConstructor
public class KinesisListenersConfig {

    private final KinesisClient kinesisClient;
    private final MessagingCosumer messagingCosumer;
    private final List<String> streamList;

    //@Bean
    public List<KinesisMessageListener> kinesisMessageListeners() {
        return streamList.stream()
                .map(stream -> new KinesisMessageListener(kinesisClient, messagingCosumer, stream))
                .toList();
    }
}
