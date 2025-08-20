package com.example.common.configuration.messaging;

import com.example.common.configuration.messaging.kafka.KafkaMessagingConfig;
import com.example.common.configuration.messaging.kinesis.KinesisMessagingConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
public class MessagingConfiguration {

    @Configuration
    @ConditionalOnProperty(name = "messaging.provider", havingValue = "kafka")
    @Import(KafkaMessagingConfig.class)
    public static class KafkaConfiguration {
    }

    @Configuration
    @ConditionalOnProperty(name = "messaging.provider", havingValue = "kinesis")
    @Import(KinesisMessagingConfig.class)
    public static class KinesisConfiguration {
    }
}
