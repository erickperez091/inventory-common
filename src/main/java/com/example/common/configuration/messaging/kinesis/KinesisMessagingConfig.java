package com.example.common.configuration.messaging.kinesis;

import com.example.common.service.messaging.MessagingCosumer;
import com.example.common.service.messaging.MessagingProducer;
import com.example.common.service.messaging.impl.KinesisAsyncMessageProducer;
import com.example.common.service.messaging.impl.KinesisMessageProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.kinesis.common.ConfigsBuilder;
import software.amazon.kinesis.coordinator.Scheduler;

import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@ConditionalOnProperty(name = "messaging.provider", havingValue = "kinesis")
@EnableScheduling
@Log4j2
public class KinesisMessagingConfig {

    @Value("${messaging.kinesis.stream-name}")
    private String streamName;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${messaging.kinesis.initial-position:TRIM_HORIZON}")
    private String initialPosition;

    private static final ObjectMapper mapper = new ObjectMapper();

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


    // This doesn't work with localstack
    //@Bean(destroyMethod = "shutdown")
    public Scheduler kinesisScheduler(KinesisAsyncClient kinesisAsyncClient, DynamoDbAsyncClient dynamoDbAsyncClient, CloudWatchAsyncClient cloudWatchAsyncClient, MessagingCosumer messagingCosumer) {

        String workerId = UUID.randomUUID().toString();

        KinesisRecordProcessorFactory factory =
                new KinesisRecordProcessorFactory(messagingCosumer, mapper);

        ConfigsBuilder configsBuilder = new ConfigsBuilder(
                streamName,
                applicationName,
                kinesisAsyncClient,
                dynamoDbAsyncClient,
                cloudWatchAsyncClient,
                workerId,
                factory
        );

        /*InitialPositionInStream positionEnum = InitialPositionInStream
                .valueOf(initialPosition.toUpperCase(Locale.ROOT));

        configsBuilder.retrievalConfig()
                .initialPositionInStreamExtended(
                        InitialPositionInStreamExtended.newInitialPosition(positionEnum)
                )
                .retrievalSpecificConfig(new PollingConfig(streamName, kinesisAsyncClient)
                        .idleTimeBetweenReadsInMillis(200) // baja latencia en local
                );*/

        Scheduler scheduler = new Scheduler(
                configsBuilder.checkpointConfig(),
                configsBuilder.coordinatorConfig(),
                configsBuilder.leaseManagementConfig(),
                configsBuilder.lifecycleConfig(),
                configsBuilder.metricsConfig(),
                configsBuilder.processorConfig(),
                configsBuilder.retrievalConfig()
        );


        Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "kcl-scheduler-" + workerId.substring(0, 8));
            t.setDaemon(true);
            return t;
        }).submit(scheduler);

        return scheduler;
    }


}
