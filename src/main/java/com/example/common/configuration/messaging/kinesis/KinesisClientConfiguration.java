package com.example.common.configuration.messaging.kinesis;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisClient;

import java.net.URI;
import java.util.concurrent.Executor;

@Configuration
@ConditionalOnProperty(name = "messaging.provider", havingValue = "kinesis")
@Log4j2
public class KinesisClientConfiguration {

    @Value("${messaging.kinesis.endpoint-override:}")
    private String endpointOverride;

    @PostConstruct
    public void init() {
        logger.info("[KinesisProducerConfiguration]: Creating Class Beans KinesisProducerConfiguration");
    }

    @Bean
    public KinesisClient kinesisClient() {
        return KinesisClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("accessKey", "secretKey")))
                .endpointOverride(URI.create(endpointOverride))
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "messaging.kinesis.async-native", havingValue = "true")
    public KinesisAsyncClient kinesisAsyncClient() {
        return KinesisAsyncClient.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(URI.create(endpointOverride))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("accessKey", "secretKey")))
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "messaging.kinesis.async-native", havingValue = "true")
    public CloudWatchAsyncClient cloudWatchAsyncClient() {
        return CloudWatchAsyncClient.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(URI.create(endpointOverride))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("accessKey", "secretKey")))
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "messaging.kinesis.async-native", havingValue = "true")
    public DynamoDbAsyncClient dynamoDbAsyncClient() {
        DynamoDbAsyncClient client = DynamoDbAsyncClient.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(URI.create(endpointOverride))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("accessKey", "secretKey")))
                .build();

        try {
            client.deleteTable(DeleteTableRequest.builder()
                    .tableName("user-service")
                    .build()).join();
        } catch (Exception ignored) {
        }


        return client;
    }

    @Bean(name = "kinesisExecutor")
    @ConditionalOnProperty(name = "messaging.kinesis.async-native", havingValue = "false")
    public Executor kinesisExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("KinesisMessageProducer-");
        executor.initialize();
        return executor;
    }
}
