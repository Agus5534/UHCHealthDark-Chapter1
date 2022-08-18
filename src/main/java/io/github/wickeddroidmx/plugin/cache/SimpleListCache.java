package io.github.wickeddroidmx.plugin.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleListCache<K> implements ListCache<K> {

    private final List<K> list = new ArrayList<>();

    @Override
    public void add(K key) {
        list.add(key);
    }

    @Override
    public void remove(K key) {
        list.remove(key);
    }

    @Override
    public K remove(int index) {
        return list.remove(index);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public K get(int key) {
        return list.get(key);
    }

    @Override
    public boolean exists(K key) {
        return list.contains(key);
    }

    @Override
    public List<K> values() {
        return list;
    }
}
