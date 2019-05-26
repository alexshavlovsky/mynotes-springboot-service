package com.ctzn.mynotesservice;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Util {

    private Util() {
        throw new AssertionError();
    }

    // this provides a functionality similar to Java 9 Map.of(Object... )
    public static <K, V> Map<K, V> mapOf(Object... input) {
        Map<K, V> res = new HashMap<>();
        for (int i = 0; i < input.length; i += 2) {
            @SuppressWarnings("unchecked")
            K k = Objects.requireNonNull((K) input[i]);
            @SuppressWarnings("unchecked")
            V v = Objects.requireNonNull((V) input[i + 1]);
            res.put(k, v);
        }
        return res;
    }

    public static Map customMessage(String text) {
        return mapOf("message", text);
    }

}
