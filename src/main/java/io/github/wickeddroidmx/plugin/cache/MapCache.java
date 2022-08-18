package io.github.wickeddroidmx.plugin.cache;

import java.util.Collection;

public interface MapCache<K, V> {

    void add(K key, V value);

    V get(K key);

    V remove(K key);

    void clear();

    boolean exists(K key);

    Collection<V> values();
}
