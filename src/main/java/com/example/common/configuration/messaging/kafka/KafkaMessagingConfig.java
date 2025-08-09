package com.example.common.configuration.messaging.kafka;

import com.example.common.service.messaging.MessagingCosumer;
import com.example.common.service.messaging.MessagingProducer;
import com.example.common.service.messaging.impl.KafkaMessageProducer;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@ConditionalOnProperty(name = "messaging.provider", havingValue = "kafka")
@Log4j2
public class KafkaMessagingConfig {

    @PostConstruct
    public void init() {
        logger.info("[KafkaMessagingConfig]: Creating Class Beans KafkaMessagingConfig");
    }

    @Bean
    public MessagingProducer kafkaMessagingProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        return new KafkaMessageProducer(kafkaTemplate);
    }

    @Bean
    public KafkaMessageListener kafkaMessageListener(MessagingCosumer messagingCosumer) {
        return new KafkaMessageListener(messagingCosumer);
    }
}
