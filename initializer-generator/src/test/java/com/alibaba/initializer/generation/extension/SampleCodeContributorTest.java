package com.alibaba.initializer.generation.extension;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SampleCodeContributorTest {

    @Test
    public void testDeepMerge() {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("key1", "value1");
        map1.put("key2", "value2");
        map1.put("key3", Arrays.asList("item1", "item2"));

        Map<String, Object> nestedMap1 = new HashMap<>();
        nestedMap1.put("nestedKey1", "nestedValue1");
        map1.put("nested", nestedMap1);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("key2", "new_value2");
        map2.put("key3", Arrays.asList("item3"));

        Map<String, Object> nestedMap2 = new HashMap<>();
        nestedMap2.put("nestedKey2", "nestedValue2");
        map2.put("nested", nestedMap2);

        Map<String, Object> mergedMap = SampleCodeContributor.deepMerge(map1, map2);

        System.out.println("mergedMap: " + mergedMap);
        System.out.println("map1: " + map1);
        System.out.println("map2: " + map2);

        assertEquals("value1", mergedMap.get("key1"));
        assertEquals("new_value2", mergedMap.get("key2"));

        @SuppressWarnings("unchecked")
        List<String> key3List = (List<String>) mergedMap.get("key3");
        assertEquals(3, key3List.size());
        assertEquals("item1", key3List.get(0));
        assertEquals("item2", key3List.get(1));
        assertEquals("item3", key3List.get(2));

        @SuppressWarnings("unchecked")
        Map<String, Object> nestedMergedMap = (Map<String, Object>) mergedMap.get("nested");
        assertEquals("nestedValue1", nestedMergedMap.get("nestedKey1"));
        assertEquals("nestedValue2", nestedMergedMap.get("nestedKey2"));
    }

}
