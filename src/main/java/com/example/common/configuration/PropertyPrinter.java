package com.example.common.configuration;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Log4j2
public class PropertyPrinter {

    private final ConfigurableEnvironment environment;
    private final List< String > includedPrefixes = Arrays.asList(
            "spring.datasource",
            "spring.kafka",
            "eureka",
            "kafka",
            "server.port",
            "security"
    );

    public PropertyPrinter( Environment environment ) {
        this.environment = (ConfigurableEnvironment) environment;
    }

    @PostConstruct
    public void init() {
        logger.info( "---- PROPERTIES DETECTADOS POR SPRING ----" );
        for ( PropertySource< ? > source : environment.getPropertySources() ) {
            if ( source instanceof MapPropertySource mapSource ) {
                for ( String key : mapSource.getPropertyNames() ) {
                    if ( matchesIncludedPrefix( key ) ) {
                        String value = environment.getProperty( key );
                        logger.info("{} = {}", key, value);
                    }
                }
            }
        }
        logger.info( "---- FIN DE LISTA ----" );
    }

    private boolean matchesIncludedPrefix( String key ) {
        return includedPrefixes.stream().anyMatch( key::startsWith );
    }
}
