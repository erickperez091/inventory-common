package com.example.common.service;

import com.example.common.entity.MessageEvent;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@ConditionalOnProperty(name = "enable.kafka", havingValue = "true")
@Log4j2
public class KafkaSenderService {

    private final KafkaTemplate< String, Object > kafkaTemplate;

    @Value( "${topic-name}" )
    private String topic;

    @Autowired
    KafkaSenderService( KafkaTemplate< String, Object > kafkaTemplate ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage( MessageEvent messageEvent ) {
        logger.info( "Start sending message to [{}] topic, message: {}", topic, messageEvent );
        ProducerRecord< String, Object > producerRecord = new ProducerRecord<>( topic, messageEvent );
        CompletableFuture<SendResult<String, Object>> response = kafkaTemplate.send( producerRecord );
        response.whenComplete( (result, ex) -> {
            if ( ex != null ) {
                logger.error( "Unable to send message=[{}] due to : {}", messageEvent, ex.getMessage() );
            }
            else{
                logger.info( "Sent message=[{}] with offset=[{}]", messageEvent, result.getRecordMetadata().offset() );
            }
        } );

        /*ListenableFuture< SendResult< String, Object > > future = kafkaTemplate.send( producerRecord );
        future.addCallback( new ListenableFutureCallback<>() {
            @Override
            public void onSuccess( SendResult< String, Object > result ) {
                logger.info( "Sent message=[{}] with offset=[{}]", messageEvent, result.getRecordMetadata().offset() );
            }

            @Override
            public void onFailure( Throwable ex ) {
                logger.error( "Unable to send message=[{}] due to : {}", messageEvent, ex.getMessage() );
            }
        } );*/
        logger.info( "Finish sending message to [{}] topic, message: {}", topic, messageEvent );
    }
}
