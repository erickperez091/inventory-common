package com.example.common.utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConverterUtilTest {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    static class Dummy {
        private String name;
        private int age;
    }

    @Test
    void objectToMap_returnsMapWithFields() {
        ConverterUtil util = new ConverterUtil();
        Dummy dummy = new Dummy("John", 30);

        Map<String, Object> map = util.objectToMap(dummy);

        assertEquals("John", map.get("name"));
        assertEquals(30, map.get("age"));
    }

    @Test
    void mapToObject_returnsObjectWithFields() {
        ConverterUtil util = new ConverterUtil();
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Jane");
        map.put("age", 25);

        Dummy dummy = util.mapToObject(map, Dummy.class);

        assertEquals("Jane", dummy.name);
        assertEquals(25, dummy.age);
    }

    @Test
    void copyProperties_copiesFieldsExceptIgnored() {
        ConverterUtil util = new ConverterUtil();
        Dummy source = new Dummy("Alice", 40);
        Dummy target = new Dummy();

        util.copyProperties(source, target, "age");

        assertEquals("Alice", target.name);
        assertEquals(0, target.age); // age is ignored
    }

    @Test
    void transformObject_transformsToTargetType() {
        ConverterUtil util = new ConverterUtil();
        Dummy source = new Dummy("Bob", 50);

        TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {
        };
        Map<String, Object> result = util.transformObject(source, typeRef);

        assertEquals("Bob", result.get("name"));
        assertEquals(50, result.get("age"));
    }
}