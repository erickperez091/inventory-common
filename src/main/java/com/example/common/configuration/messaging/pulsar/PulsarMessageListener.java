package com.example.common.configuration.messaging.pulsar;

import com.example.common.entity.MessageEvent;
import com.example.common.service.messaging.MessageConsumerRouter;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;

@Log4j2
@RequiredArgsConstructor
public class PulsarMessageListener {

    private final PulsarClient pulsarClient;
    private final MessageConsumerRouter router;
    private final String[] topics;
    @Value("${messaging.pulsar.subscription-name}")
    private String subscriptionName;


    @PostConstruct
    public void init() throws PulsarClientException {
        logger.info("[PulsarMessageListener]: Creating Class Beans PulsarMessageListener");

        Arrays.stream(topics).forEach(topic -> {
            try {
                this.pulsarClient.newConsumer(Schema.JSON(MessageEvent.class))
                        .topic(topic)
                        .subscriptionName(subscriptionName)
                        .messageListener((consumer, message) -> {
                            try {
                                this.router.route(topic, message.getValue());
                                consumer.acknowledge(message);
                            } catch (PulsarClientException e) {
                                logger.error("[PulsarMessageListener][Init][Error] Error processing message from {}", topic);
                                consumer.negativeAcknowledge(message);
                            }
                        }).subscribe();
            } catch (PulsarClientException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
