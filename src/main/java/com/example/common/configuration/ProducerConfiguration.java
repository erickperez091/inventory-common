package com.example.common.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProducerConfiguration {

    @Value( value = "${kafka.bootstrapAddress}" )
    private String bootstrapAddress;

    @Value( value = "${kafka.consumer.client-id}" )
    private String kafkaId;

    @Bean
    public Map< String, Object > producerConfigs() {
        Map< String, Object > props = new HashMap<>();
        props.put( ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress );
        props.put( ProducerConfig.CLIENT_ID_CONFIG, kafkaId );
        props.put( ProducerConfig.ACKS_CONFIG, "all" );
        props.put( ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class );
        props.put( ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class );
        return props;
    }

    @Bean
    public ProducerFactory< String, Object > producerFactory() {
        return new DefaultKafkaProducerFactory<>( producerConfigs() );
    }

    @Bean
    public KafkaTemplate< String, Object > kafkaTemplate() {
        return new KafkaTemplate<>( producerFactory() );
    }

}
