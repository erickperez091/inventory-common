package com.example.common.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Log4j2
public class WebClientConf
{
    @Bean(name = "webClientLoadBalanced")
    @LoadBalanced
    @Primary
    public WebClient.Builder getWebClient(){
        try {
            logger.info("[WebClientConf][getWebClient][Start] Creating Load Balanced Web Client Builder");
            return WebClient.builder();
        }
        finally {
            logger.info("[WebClientConf][getWebClient][End] Creating Load Balanced Web Client Builder");
        }
    }


    @Bean(name = "normalWebClient")
    public WebClient.Builder getNormalWebClient(){
        try {
            logger.info("[WebClientConf][getWebClient][Start] Creating Web Client Builder");
            return WebClient.builder();
        }
        finally {
            logger.info("[WebClientConf][getWebClient][End] Creating Web Client Builder");
        }
    }
}
