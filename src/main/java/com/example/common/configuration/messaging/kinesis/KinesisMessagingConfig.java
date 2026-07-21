package com.example.common.configuration.messaging.kinesis;

import com.example.common.service.messaging.MessageConsumerRouter;
import com.example.common.service.messaging.MessagingConsumer;
import com.example.common.service.messaging.MessagingProducer;
import com.example.common.service.messaging.impl.kinesis.KinesisAsyncMessageProducer;
import com.example.common.service.messaging.impl.kinesis.KinesisMessageProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;
import java.util.concurrent.Executor;

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

    /// This implements a way to handle N destinations in the same microservice ///
    @Bean
    @ConditionalOnProperty(name = "messaging.kinesis.async-native", havingValue = "false")
    public MessagingProducer kinesisMessagingProducer(final KinesisClient kinesisClient, @Qualifier("kinesisExecutor") Executor executor) {
        return new KinesisMessageProducer(kinesisClient, executor);
    }

    @Bean
    @ConditionalOnProperty(name = "messaging.kinesis.async-native", havingValue = "true")
    public MessagingProducer kinesisAsyncMessagingProducer(final KinesisAsyncClient kinesisAsyncClient) {
        return new KinesisAsyncMessageProducer(kinesisAsyncClient);
    }

    @Bean
    public KinesisMessageListener kinesisMessageListener(KinesisClient client, MessageConsumerRouter router, List<MessagingConsumer> consumers) {
        return new KinesisMessageListener(client, router, consumers);
    }

    /*

     /// This implements a way to handle only 1 destination in the same microservice ///

    //@Bean
    //@ConditionalOnProperty(name = "messaging.kinesis.async-native", havingValue = "false")
    public MessagingProducerV1 kinesisMessagingProducer(final KinesisClient kinesisClient, @Qualifier("kinesisExecutor") Executor executor) {
        return new KinesisMessageProducerV1(kinesisClient, executor);
    }

    // @Bean
    //@ConditionalOnProperty(name = "messaging.kinesis.async-native", havingValue = "true")
    public MessagingProducerV1 kinesisAsyncMessagingProducer(final KinesisAsyncClient kinesisAsyncClient) {
        return new KinesisAsyncMessageProducerV1(kinesisAsyncClient);
    }

    //@Bean
    public KinesisMessageListenerV1 kinesisMessageListener(final KinesisClient kinesisClient, final MessagingCosumerV1 messagingCosumerV1) {
        return new KinesisMessageListenerV1(kinesisClient, messagingCosumerV1, streamName);
    }

     */


    // This doesn't work with localstack
    //@Bean(destroyMethod = "shutdown")
    /*public Scheduler kinesisScheduler(KinesisAsyncClient kinesisAsyncClient, DynamoDbAsyncClient dynamoDbAsyncClient, CloudWatchAsyncClient cloudWatchAsyncClient, MessagingCosumer messagingCosumer) {

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

        InitialPositionInStream positionEnum = InitialPositionInStream
                .valueOf(initialPosition.toUpperCase(Locale.ROOT));

        configsBuilder.retrievalConfig()
                .initialPositionInStreamExtended(
                        InitialPositionInStreamExtended.newInitialPosition(positionEnum)
                )
                .retrievalSpecificConfig(new PollingConfig(streamName, kinesisAsyncClient)
                        .idleTimeBetweenReadsInMillis(200) // baja latencia en local
                );

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
    */
}
