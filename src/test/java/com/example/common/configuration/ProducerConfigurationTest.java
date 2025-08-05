package com.example.common.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.jupiter.api.BeforeEach;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProducerConfigurationTest {

    private ProducerConfiguration producerConfiguration;

    @BeforeEach
    void setUp() {
        producerConfiguration = new ProducerConfiguration();
        producerConfiguration.setBootstrapAddress("localhost:9092");
        producerConfiguration.setKafkaId("test-client");
    }

    @Test
    void producerConfigs_returnsExpectedConfig() {
        Map<String, Object> configs = producerConfiguration.producerConfigs();

        //
        assertNotNull(configs);
        assertEquals("localhost:9092", configs.get(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));
        assertEquals("test-client", configs.get(ProducerConfig.CLIENT_ID_CONFIG));
        assertEquals("all", configs.get(ProducerConfig.ACKS_CONFIG));
        assertEquals(StringSerializer.class, configs.get(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG));
        assertEquals(JsonSerializer.class, configs.get(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG));
    }

    @Test
    void producerFactory_isCreatedSuccessfully() {
        ProducerFactory<String, Object> factory = producerConfiguration.producerFactory();
        assertNotNull(factory);
    }

    @Test
    void kafkaTemplate_isCreatedSuccessfully() {
        KafkaTemplate<String, Object> template = producerConfiguration.kafkaTemplate();
        assertNotNull(template);
    }

}
