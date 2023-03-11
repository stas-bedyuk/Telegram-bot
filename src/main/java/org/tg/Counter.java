package org.tg;
import java.util.HashMap;
import java.util.Map;

public class Counter {
    private static Counter instance;
    private Map<Long, int[]> counter;

    private Counter() {
        counter = new HashMap<>();
    }

    public static Counter getInstance() {
        if (instance == null) {
            instance = new Counter();
        }
        return instance;
    }

    public void put(Long key, int[] value) {
        counter.put(key, value);
    }

    public int[] get(Long key) {
        return counter.get(key);
    }
    public void delete(Long key) {
        counter.remove(key);
    }
}