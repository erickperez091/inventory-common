package com.example.common.configuration.messaging.kafka;

import com.example.common.entity.MessageEvent;
import com.example.common.service.messaging.MessagingCosumer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class KafkaListenersConfig {

    private List<String> topics;
    private String groupId;

    private final ConcurrentKafkaListenerContainerFactory<String, MessageEvent> kafkaListenerContainerFactory;
    private final MessagingCosumer messagingCosumer;

    //@Bean
    public List<MessageListenerContainer> kafkaListenerContainer() {
        List<MessageListenerContainer> container = new ArrayList<>();
        topics.forEach(topic -> {
            ContainerProperties containerProperties = new ContainerProperties(topic);
            containerProperties.setGroupId(groupId);
            containerProperties
                    .setMessageListener((MessageListener<String, MessageEvent>)
                            message -> messagingCosumer.consume(message.value()));
            KafkaMessageListenerContainer<String, MessageEvent> listenerContainer =
                    new KafkaMessageListenerContainer<>(kafkaListenerContainerFactory.getConsumerFactory(), containerProperties);

            listenerContainer.start();
            container.add(listenerContainer);
        });

        return container;
    }

    //@Bean
    public List<KafkaMessageListenerV2> kafkaMessageListeners() {
        return topics.stream()
                .map(topic -> new KafkaMessageListenerV2(messagingCosumer, topic, groupId))
                .toList();
    }
}
