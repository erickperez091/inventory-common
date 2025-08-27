package com.example.common.configuration;

class ConsumerConfigurationTest {

    /*private KafkaProperties kafkaProperties;
    private ConsumerConfiguration consumerConfiguration;

    @BeforeEach
    void setUp() {
        kafkaProperties = mock(KafkaProperties.class);
        when(kafkaProperties.buildConsumerProperties()).thenReturn(Map.of());
        consumerConfiguration = new ConsumerConfiguration(kafkaProperties);
        consumerConfiguration.setBootstrapAddress("localhost:9092");
        consumerConfiguration.setKafkaId("test-client");
        consumerConfiguration.setMaxPollRecords(10);
    }

    @Test
    void consumerFactory_createsFactoryWithCorrectProperties() {
        ConsumerFactory<String, MessageEvent> factory = consumerConfiguration.consumerFactory();
        assertNotNull(factory);
    }

    @Test
    void kafkaListenerContainerFactory_createsFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MessageEvent> factory =
                consumerConfiguration.kafkaListenerContainerFactory();
        assertNotNull(factory);
        assertNotNull(factory.getConsumerFactory());
    }*/
}