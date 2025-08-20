package com.example.common.service;

import com.example.common.entity.EnumUtil.EventType;
import com.example.common.entity.MessageEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class KafkaSenderServiceTest {

    /*@Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private KafkaSenderService kafkaSenderService;

    @Captor
    private ArgumentCaptor<ProducerRecord<String, Object>> recordCaptor;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        Field topicField = KafkaSenderService.class.getDeclaredField("topic");
        topicField.setAccessible(true);
        topicField.set(kafkaSenderService, "test-topic");
    }

    @Test
    void testSendMessage_shouldSendToKafkaTemplate() {
        MessageEvent event = new MessageEvent(
                EventType.CREATE_PRODUCT,
                Map.of("userId", 123, "name", "Erick")
        );

        SendResult<String, Object> mockResult = mock(SendResult.class);
        RecordMetadata metadata = mock(RecordMetadata.class);
        when(mockResult.getRecordMetadata()).thenReturn(metadata);

        CompletableFuture<SendResult<String, Object>> future = CompletableFuture.completedFuture(mockResult);
        when(kafkaTemplate.send(any(ProducerRecord.class))).thenReturn(future);

        // Act
        kafkaSenderService.sendMessage(event);

        // Assert
        verify(kafkaTemplate).send(recordCaptor.capture());
        ProducerRecord<String, Object> sentRecord = recordCaptor.getValue();

        assertEquals("test-topic", sentRecord.topic());
        assertEquals(event, sentRecord.value());
    }

    @Test
    void sendMessage_shouldHandleSendFailureGracefully() {
        // Arrange
        MessageEvent event = new MessageEvent(EventType.CREATE_PRODUCT, Map.of("error", "true"));

        CompletableFuture<SendResult<String, Object>> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("Kafka is unavailable"));

        when(kafkaTemplate.send(any(ProducerRecord.class))).thenReturn(failedFuture);

        // Act & Assert
        assertDoesNotThrow(() -> kafkaSenderService.sendMessage(event));
        verify(kafkaTemplate).send(any(ProducerRecord.class));
    }*/

}
