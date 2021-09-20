package com.fjace.pay.core.base.util;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 忽略字符大小写
 * @date 2021-09-04 16:59:00
 */
public class CaseInsensitiveMap <V> extends AbstractMap<String, V>
        implements Map<String, V>, Cloneable, Serializable {
    private static final long serialVersionUID = -4267891064841845539L;

    private final Map<String, Entry<String, V>> store;

    public CaseInsensitiveMap() {
        this.store = new HashMap<String, Entry<String, V>>();
    }

    public static <V> CaseInsensitiveMap<V> of(Map<String, V> map) {
        if (map == null) {
            return null;
        }
        CaseInsensitiveMap<V> ciMap = new CaseInsensitiveMap<>();
        ciMap.putAll(map);
        return ciMap;
    }

    @Override
    public boolean containsKey(Object key) {
        String keyLower = convertKey(key);
        return this.store.containsKey(keyLower);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.containsValue(value);
    }

    @Override
    public V get(Object key) {
        String keyLower = convertKey(key);
        Entry<String, V> entry = this.store.get(keyLower);
        if (entry == null) {
            return null;
        }
        return entry.getValue();
    }

    @Override
    public V put(String key, V value) {
        String keyLower = convertKey(key);
        this.store.put(keyLower, new SimpleEntry<String, V>(key, value));
        return value;
    }

    @Override
    public V remove(Object key) {
        String keyLower = convertKey(key);
        Entry<String, V> entry = this.store.remove(keyLower);
        if (entry == null) {
            return null;
        }
        return entry.getValue();
    }

    @Override
    public void clear() {
        this.store.clear();
    }

    @Override
    public Set<String> keySet() {
        return this.store.values().stream().map(Entry::getKey).collect(Collectors.toSet());
    }

    @Override
    public Collection<V> values() {
        return this.store.values().stream().map(Entry::getValue).collect(Collectors.toList());
    }

    @Override
    public Set<Entry<String, V>> entrySet() {
        return new HashSet<>(this.store.values());
    }

    private static String convertKey(Object key) {
        if (key == null) {
            return null;
        } else if (key instanceof String) {
            return ((String) key).toLowerCase();
        }
        throw new IllegalArgumentException("key must be a String");
    }
}
