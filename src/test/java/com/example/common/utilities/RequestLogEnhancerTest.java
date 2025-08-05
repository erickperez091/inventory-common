package com.example.common.utilities;

import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpHeader;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RequestLogEnhancerTest {

    @Test
    void toString_withArrayBackedBuffer_returnsString() throws Exception {
        String text = "hello";
        ByteBuffer buffer = ByteBuffer.wrap(text.getBytes(StandardCharsets.UTF_8));
        String result = invokeToString(buffer, StandardCharsets.UTF_8);
        assertEquals(text, result);
    }

    @Test
    void toString_withNonArrayBackedBuffer_returnsString() throws Exception {
        ByteBuffer buffer = ByteBuffer.allocateDirect(5);
        buffer.put("world".getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        String result = invokeToString(buffer, StandardCharsets.UTF_8);
        assertEquals("world", result);
    }

    @Test
    void getCharset_withCharsetHeader_returnsCorrectCharset() throws Exception {
        HttpFields headers = mock(HttpFields.class);
        when(headers.get(HttpHeader.CONTENT_TYPE)).thenReturn("application/json; charset=UTF-8");
        Charset charset = invokeGetCharset(headers);
        assertEquals(StandardCharsets.UTF_8, charset);
    }

    // Helper methods to access private static methods via reflection
    private String invokeToString(ByteBuffer buffer, Charset charset) throws Exception {
        var method = RequestLogEnhancer.class.getDeclaredMethod("toString", ByteBuffer.class, Charset.class);
        method.setAccessible(true);
        return (String) method.invoke(null, buffer, charset);
    }

    private Charset invokeGetCharset(HttpFields headers) throws Exception {
        var method = RequestLogEnhancer.class.getDeclaredMethod("getCharset", HttpFields.class);
        method.setAccessible(true);
        return (Charset) method.invoke(null, headers);
    }
}
