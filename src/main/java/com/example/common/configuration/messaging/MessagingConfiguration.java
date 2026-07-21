package com.example.common.configuration.messaging;

import com.example.common.configuration.messaging.kafka.KafkaMessagingConfig;
import com.example.common.configuration.messaging.kinesis.KinesisMessagingConfig;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
public class MessagingConfiguration {

    @Configuration
    @ConditionalOnProperty(name = "messaging.provider", havingValue = "kafka")
    @Import(KafkaMessagingConfig.class)
    @Log4j2
    public static class KafkaConfiguration {

        @PostConstruct
        public void init() {
            logger.info("[KafkaConfiguration]: Creating Class Beans KafkaConfiguration");
        }
    }

    @Configuration
    @ConditionalOnProperty(name = "messaging.provider", havingValue = "kinesis")
    @Import(KinesisMessagingConfig.class)
    @Log4j2
    public static class KinesisConfiguration {

        @PostConstruct
        public void init() {
            logger.info("[KinesisConfiguration]: Creating Class Beans KinesisConfiguration");
        }
    }
}
