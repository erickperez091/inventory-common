package com.example.common.configuration;

import com.example.common.entitty.MessageEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConsumerConfiguration {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Value( value = "${kafka.bootstrapAddress}" )
    private String bootstrapAddress;

    @Value( value = "${kafka.consumer.client-id}" )
    private String kafkaId;

    @Value( value = "${kafka.max.poll.records}" )
    private int maxPollRecords;
//    @Bean
//    public Map<String, Object> consumerConfigs() {
//        return props;
//    }

    @Bean
    public ConsumerFactory< String, MessageEvent > consumerFactory() {
        Map< String, Object > props = new HashMap<>(
                kafkaProperties.buildConsumerProperties()
        );

        JsonDeserializer< MessageEvent > deserializer = new JsonDeserializer<>( MessageEvent.class );
        deserializer.setRemoveTypeHeaders( false );
        deserializer.addTrustedPackages( "*" );
        deserializer.setUseTypeMapperForKey( true );

        props.put( ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress );
        props.put( ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords );
        props.put( ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class );
        props.put( ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer );
        props.put( ConsumerConfig.GROUP_ID_CONFIG, kafkaId );
        return new DefaultKafkaConsumerFactory<>( props, new StringDeserializer(), deserializer );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory< String, MessageEvent > kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory< String, MessageEvent > factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory( consumerFactory() );
        return factory;
    }
}
