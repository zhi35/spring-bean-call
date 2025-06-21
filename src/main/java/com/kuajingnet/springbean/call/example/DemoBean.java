package com.kuajingnet.springbean.call.example;

import java.util.HashMap;
import java.util.Map;

public class DemoBean {
    public Map<String, String> testCallPublic(String p) {
        Map<String, String> map = new HashMap<>();
        map.put("param", p);
        return map;
    }

    private Map<String, String> testCallPrivate(String p) {
        Map<String, String> map = new HashMap<>();
        map.put("param", p);
        return map;
    }
}
