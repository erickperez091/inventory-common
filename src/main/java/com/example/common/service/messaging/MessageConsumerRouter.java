package com.example.common.service.messaging;

import com.example.common.entity.MessageEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j2
@Component
public class MessageConsumerRouter {

    private final Map<String, MessagingConsumer> consumers;

    public MessageConsumerRouter(List<MessagingConsumer> consumers) {
        this.consumers = consumers.stream()
                .collect(Collectors.toMap(
                        MessagingConsumer::destination,
                        Function.identity(),
                        (a, b) -> {
                            throw new IllegalStateException(
                                    "Duplicate consumer for destination: " + a.destination()
                            );
                        }
                ));
    }

    public void route(String destination, MessageEvent messageEvent) {
        MessagingConsumer consumer = consumers.get(destination);

        if (Objects.isNull(consumer)) {
            logger.warn("No consumer registered for destination [{}]", destination);
            return;
        }

        consumer.consume(messageEvent);
    }
}
