package dev.lotnest.dernbot.core.utils;

import com.fasterxml.jackson.databind.JsonNode;

public final class JsonUtils {
    private JsonUtils() {
    }

    public static String getNullableString(JsonNode node, String fieldName) {
        if (node == null) {
            return null;
        }
        return node.has(fieldName) && !node.get(fieldName).isNull() ? node.get(fieldName).asText() : null;
    }

    public static int getIntSafely(JsonNode node, String fieldName) {
        if (node == null) {
            return 0;
        }
        return node.has(fieldName) ? node.get(fieldName).asInt() : 0;
    }

    public static long getLongSafely(JsonNode node, String fieldName) {
        if (node == null) {
            return 0L;
        }
        return node.has(fieldName) ? node.get(fieldName).asLong() : 0L;
    }
}
