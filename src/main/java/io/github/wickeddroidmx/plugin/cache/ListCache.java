package io.github.wickeddroidmx.plugin.cache;

import java.util.List;

public interface ListCache<K> {

    void add(K key);

    void remove(K key);

    K remove(int key);

    int size();

    K get(int i);

    boolean exists(K key);

    List<K> values();

}
