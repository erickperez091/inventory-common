package com.example.common.utilities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PropertiesUtilTest {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    static class TestBean {
        private String a;
        private Integer b = 42;
        private String c;

    }

    @Test
    void getNullProperties_returnsNullPropertyNames() {
        PropertiesUtil util = new PropertiesUtil();
        TestBean bean = new TestBean();
        String[] nullProps = util.getNullProperties(bean);

        // Should contain "a" and "c"
        assertTrue(java.util.Arrays.asList(nullProps).contains("a"));
        assertTrue(java.util.Arrays.asList(nullProps).contains("c"));
        // Should not contain "b"
        assertFalse(java.util.Arrays.asList(nullProps).contains("b"));
    }

    @Test
    void getNullProperties_returnsEmptyArrayIfNoNulls() {
        PropertiesUtil util = new PropertiesUtil();
        TestBean bean = new TestBean();
        bean.setA("value");
        bean.setC("value");
        String[] nullProps = util.getNullProperties(bean);

        assertEquals(0, nullProps.length);
    }

    @Test
    void getNullProperties_throwsExceptionForNullObject() {
        PropertiesUtil util = new PropertiesUtil();
        assertThrows(IllegalArgumentException.class, () -> util.getNullProperties(null));
    }

}
