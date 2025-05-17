package com.example.common.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConverterUtil {

    private static final Logger logger = LoggerFactory.getLogger( ConverterUtil.class );

    private static final ObjectMapper mapper = new ObjectMapper();

    public Map< String, Object > objectToMap( Object object ) {
        logger.info( "START | Transform Pojo {} to Map", object.getClass().getName() );
        Map< String, Object > map = mapper.convertValue( object, new TypeReference<>() {
        } );
        logger.info( "FINISH | Transform Pojo {} to Map", object.getClass().getName() );
        return map;
    }

    public < T > T mapToObject( Map< String, Object > map, Class< T > clazz ) {
        logger.info( "START | Transform Map to Pojo {}", clazz.getName() );
        T t = mapper.convertValue( map, clazz );
        logger.info( "FINISH | Transform Map to Pojo {}", clazz.getName() );
        return t;
    }

    public void copyProperties( Object source, Object target, String... propsToIgnore ) {
        BeanUtils.copyProperties( source, target, propsToIgnore );
    }

    public <T, U> U transformObject(T sourceObject, TypeReference<U> targetTypeReference) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure( SerializationFeature.FAIL_ON_EMPTY_BEANS, false );
        U targetObject = null;
        try {
            String jsonString = objectMapper.writeValueAsString(sourceObject);
            targetObject = objectMapper.readValue(jsonString, targetTypeReference);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return targetObject;
    }
}
