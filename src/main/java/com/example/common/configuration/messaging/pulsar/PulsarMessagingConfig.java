package com.example.common.configuration.messaging.pulsar;

import com.example.common.entity.MessageEvent;
import com.example.common.service.messaging.MessageConsumerRouter;
import com.example.common.service.messaging.MessagingConsumer;
import com.example.common.service.messaging.MessagingProducer;
import com.example.common.service.messaging.impl.PulsarMessageProducer;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.apache.pulsar.client.api.PulsarClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.pulsar.core.PulsarTemplate;

import java.util.List;

@ConditionalOnProperty(name = "messaging.provider", havingValue = "pulsar")
@Configuration
@Log4j2
public class PulsarMessagingConfig {

    @PostConstruct
    public void init() {
        logger.info("[PulsarMessagingConfig]: Creating Class Beans PulsarMessagingConfig");
    }

    @Bean
    public String[] pulsarTopics(List<MessagingConsumer> consumers){
        return consumers.stream()
                .map(MessagingConsumer::destination)
                .distinct()
                .toArray(String[]::new);
    }

    @Bean
    public MessagingProducer pulsarMessagingProducer(PulsarTemplate<MessageEvent> pulsarTemplate){
        return new PulsarMessageProducer(pulsarTemplate);
    }

    @Bean
    public PulsarMessageListener pulsarMessageListener(MessageConsumerRouter messageConsumerRouter, PulsarClient pulsarClient, String[] pulsarTopics){
        return new PulsarMessageListener(pulsarClient, messageConsumerRouter, pulsarTopics);
    }
}
