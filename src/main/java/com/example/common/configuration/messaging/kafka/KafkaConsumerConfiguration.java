package com.example.common.configuration.messaging.kafka;

import com.example.common.entity.MessageEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty(name = "messaging.provider", havingValue = "kafka")
@RequiredArgsConstructor
public class KafkaConsumerConfiguration {

    private final KafkaProperties kafkaProperties;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${messaging.kafka.group-id:user-group}")
    private String groupId;

    @Value( value = "${messaging.kafka.max-poll-records}" )
    private int maxPollRecords;


    @Bean
    public ConsumerFactory< String, MessageEvent > consumerFactory() {
        Map< String, Object > props = new HashMap<>(
                kafkaProperties.buildConsumerProperties()
        );

        JsonDeserializer< MessageEvent > deserializer = new JsonDeserializer<>( MessageEvent.class );
        deserializer.setRemoveTypeHeaders( false );
        deserializer.addTrustedPackages( "*" );
        deserializer.setUseTypeMapperForKey( true );

        props.put( ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers );
        props.put( ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords );
        props.put( ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class );
        props.put( ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer );
        props.put( ConsumerConfig.GROUP_ID_CONFIG, groupId );
        return new DefaultKafkaConsumerFactory<>( props, new StringDeserializer(), deserializer );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory< String, MessageEvent > kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory< String, MessageEvent > factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory( consumerFactory() );
        return factory;
    }


}
