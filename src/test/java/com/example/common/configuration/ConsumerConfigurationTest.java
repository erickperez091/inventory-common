package com.example.common.configuration;

import com.example.common.entity.MessageEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConsumerConfigurationTest {

    private KafkaProperties kafkaProperties;
    private ConsumerConfiguration consumerConfiguration;

    @BeforeEach
    void setUp() {
        kafkaProperties = mock(KafkaProperties.class);
        when(kafkaProperties.buildConsumerProperties()).thenReturn(Map.of());
        consumerConfiguration = new ConsumerConfiguration(kafkaProperties);
        consumerConfiguration.setBootstrapAddress("localhost:9092");
        consumerConfiguration.setKafkaId("test-client");
        consumerConfiguration.setMaxPollRecords(10);
    }

    @Test
    void consumerFactory_createsFactoryWithCorrectProperties() {
        ConsumerFactory<String, MessageEvent> factory = consumerConfiguration.consumerFactory();
        assertNotNull(factory);
    }

    @Test
    void kafkaListenerContainerFactory_createsFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MessageEvent> factory =
                consumerConfiguration.kafkaListenerContainerFactory();
        assertNotNull(factory);
        assertNotNull(factory.getConsumerFactory());
    }
}