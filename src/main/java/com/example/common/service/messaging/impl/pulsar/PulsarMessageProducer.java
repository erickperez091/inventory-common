package com.example.common.service.messaging.impl.pulsar;

import com.example.common.aspect.AddCreatedBy;
import com.example.common.entity.MessageEvent;
import com.example.common.service.messaging.MessagingProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.pulsar.core.PulsarTemplate;

@RequiredArgsConstructor
@Log4j2
public class PulsarMessageProducer implements MessagingProducer {

    private final PulsarTemplate<MessageEvent> pulsarTemplate;

    @Override
    public void send(String destination, MessageEvent messageEvent) {
        pulsarTemplate.send(destination, messageEvent);
    }
}
