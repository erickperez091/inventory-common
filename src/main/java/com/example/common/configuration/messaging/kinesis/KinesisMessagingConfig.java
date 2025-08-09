package com.example.common.configuration.messaging.kinesis;

import com.example.common.service.messaging.MessagingCosumer;
import com.example.common.service.messaging.MessagingProducer;
import com.example.common.service.messaging.impl.KinesisAsyncMessageProducer;
import com.example.common.service.messaging.impl.KinesisMessageProducer;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisClient;

import java.util.concurrent.Executor;

@Configuration
@ConditionalOnProperty(name = "messaging.provider", havingValue = "kinesis")
@EnableScheduling
@Log4j2
public class KinesisMessagingConfig {

    @Value("${messaging.kinesis.stream-name}")
    private String streamName;

    @PostConstruct
    public void init() {
        logger.info("[KinesisMessagingConfig]: Creating Class Beans KinesisMessagingConfig");
    }

    @Bean
    @ConditionalOnProperty(name = "messaging.kinesis.async-native", havingValue = "true")
    public MessagingProducer kinesisAsyncMessagingProducer(final KinesisAsyncClient kinesisAsyncClient) {
        return new KinesisAsyncMessageProducer(kinesisAsyncClient);
    }

    @Bean
    @ConditionalOnProperty(name = "messaging.kinesis.async-native", havingValue = "false")
    public MessagingProducer kinesisMessagingProducer(final KinesisClient kinesisClient, @Qualifier("kinesisExecutor") Executor executor) {
        return new KinesisMessageProducer(kinesisClient, executor);
    }

    @Bean
    public KinesisMessageListener kinesisMessageListener(final KinesisClient kinesisClient, final MessagingCosumer messagingCosumer) {
        return new KinesisMessageListener(kinesisClient, messagingCosumer, streamName);
    }

}
