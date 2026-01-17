package com.example.common.configuration.messaging.kafka;

import com.example.common.entity.MessageEvent;
import com.example.common.service.messaging.MessageConsumerRouter;
import com.example.common.service.messaging.MessagingConsumer;
import com.example.common.service.messaging.MessagingCosumerV1;
import com.example.common.service.messaging.MessagingProducerV1;
import com.example.common.service.messaging.MessagingProducer;
import com.example.common.service.messaging.impl.KafkaMessageProducerV1;
import com.example.common.service.messaging.impl.KafkaMessageProducer;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

@Configuration
@ConditionalOnProperty(name = "messaging.provider", havingValue = "kafka")
@Log4j2
public class KafkaMessagingConfig {

    @PostConstruct
    public void init() {
        logger.info("[KafkaMessagingConfig]: Creating Class Beans KafkaMessagingConfig");
    }

    /// This implements a way to handle N destinations in the same microservice ///
    @Bean
    public String[] kafkaTopics(List<MessagingConsumer> consumers) {
        return consumers.stream()
                .map(MessagingConsumer::destination)
                .distinct()
                .toArray(String[]::new);
    }

    @Bean
    public MessagingProducer kafkaMessagingProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        return new KafkaMessageProducer(kafkaTemplate);
    }

    @Bean
    public KafkaMessageListener kafkaMessageListener(MessageConsumerRouter messageConsumerRouter){
        return new KafkaMessageListener(messageConsumerRouter);
    }

    /*

    /// This implements a way to handle only 1 destination in the same microservice ///

    @Bean
    public MessagingProducerV1 kafkaMessagingProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        return new KafkaMessageProducerV1(kafkaTemplate);
    }

    @Bean
    public KafkaMessageListenerV1 kafkaMessageListener(MessagingCosumerV1 messagingCosumerV1) {
        return new KafkaMessageListenerV1(messagingCosumerV1);
    }
     */

    //@Bean
    // Not working
    public KafkaListenersConfig kafkaListenersConfig(ConcurrentKafkaListenerContainerFactory<String, MessageEvent> kafkaListenerContainerFactory, MessagingCosumerV1 messagingCosumerV1) {
        return new KafkaListenersConfig(kafkaListenerContainerFactory, messagingCosumerV1);
    }
}
