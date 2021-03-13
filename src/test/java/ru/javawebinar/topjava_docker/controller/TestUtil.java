package ru.javawebinar.topjava_docker.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class TestUtil {
    private static final HashMap<String, Object> map = new HashMap<>(Map.of(
            "sub", Base64.getEncoder().encodeToString("user@mail.ru".getBytes(StandardCharsets.UTF_8))));
    private static final Consumer<Map<String, Object>> consumer = attr -> attr.putAll(map);

    public static Consumer<Map<String, Object>> getConsumer() {
        return consumer;
    }
}
