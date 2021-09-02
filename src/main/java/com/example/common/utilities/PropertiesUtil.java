package com.example.common.utilities;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.util.stream.Stream;

public class PropertiesUtil {

    public static String[] getNullProperties ( Object object ) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl( object );
        return Stream.of( wrappedSource.getPropertyDescriptors( ) )
                .map( FeatureDescriptor::getName )
                .filter( propertyName -> wrappedSource.getPropertyValue( propertyName ) == null )
                .toArray( String[]::new );
    }
}
