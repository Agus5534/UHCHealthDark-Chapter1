package io.github.wickeddroidmx.plugin.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SimpleMapCache<K, V> implements MapCache<K,V> {

    private final Map<K, V> map = new HashMap<>();

    @Override
    public void add(K key, V value) {
        map.put(key, value);
    }

    @Override
    public V remove(K key) {
        return map.remove(key);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public boolean exists(K key) {
        return map.containsKey(key);
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }
}
