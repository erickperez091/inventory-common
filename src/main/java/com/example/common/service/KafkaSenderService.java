package com.example.common.service;

import com.example.common.entitty.MessageEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class KafkaSenderService {

    private static final Logger logger = LoggerFactory.getLogger( KafkaSenderService.class );

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value( "${topic-name}" )
    private String topic;

    public KafkaSenderService ( ) {
    }

    public void sendMessage ( MessageEvent messageEvent ) {
        logger.info( "Start sending message to [{}] topic, message: {}", topic, messageEvent );
        ProducerRecord<String, Object> producerRecord = new ProducerRecord<>( topic, messageEvent );
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send( producerRecord );
        future.addCallback( new ListenableFutureCallback<>( ) {
            @Override
            public void onSuccess ( SendResult<String, Object> result ) {
                logger.info( "Sent message=[{}] with offset=[{}]", messageEvent, result.getRecordMetadata( ).offset( ) );
            }

            @Override
            public void onFailure ( Throwable ex ) {
                logger.error( "Unable to send message=[{}] due to : {}", messageEvent, ex.getMessage( ) );
            }
        } );
        logger.info( "Finish sending message to [{}] topic, message: {}", topic, messageEvent );
    }
}
