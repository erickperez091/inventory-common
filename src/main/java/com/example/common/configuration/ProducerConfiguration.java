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

    @Value( value = "${topic-name}" )
    private String topicName;

    @Bean
    public Map<String, Object> producerConfigs ( ) {
        Map<String, Object> props = new HashMap<>( );
        props.put( ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress );
        props.put( ProducerConfig.CLIENT_ID_CONFIG, kafkaId );
        props.put( ProducerConfig.ACKS_CONFIG, "all" );
        //props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaJsonSerializer);
        props.put( ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class );
        props.put( ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class );
        return props;
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory ( ) {
        return new DefaultKafkaProducerFactory<>( producerConfigs( ) );
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate ( ) {
        return new KafkaTemplate<>( producerFactory( ) );
    }

    /*
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic1() {
        return new NewTopic(topicName, 1, (short) 1);
    }
     */
}
