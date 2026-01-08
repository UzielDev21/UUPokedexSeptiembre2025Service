package com.UU.UUPokedexSeptiembre2025Service.Util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class SynchronizedLruCache<K, V> {

    private final Map<K, V> map;

    public SynchronizedLruCache(int maxSize) {
        this.map = Collections.synchronizedMap(new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > maxSize;
            }
        });
    }

    public V get(K key) {
        return map.get(key);
    }

    public void put(K key, V value) {
        map.put(key, value);
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public int size() {
        return map.size();
    }
}
