package org.tg;
import org.checkerframework.checker.units.qual.g;

import java.util.HashMap;
import java.util.Map;

public class Context {
    private static Context instance;
    private Map<Long, Integer> context;

    private Context() {
        context = new HashMap<>();
    }

    public static Context getInstance() {
        if (instance == null) {
            instance = new Context();
        }
        return instance;
    }

    public void put(Long key, Integer value) {
        context.put(key, value);
    }

    public Object get(Long key) {
        return context.get(key);
    }
}