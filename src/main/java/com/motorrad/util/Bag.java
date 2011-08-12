package com.motorrad.util;

import java.io.Serializable;
import java.util.*;

public class Bag<K, V> implements Map<K, V>, Serializable {
    private static final long serialVersionUID = 1;
    private LinkedHashMap<K, List<V>> map;
    private int maxsize;

    public Bag() {
        this(Integer.MAX_VALUE);
    }

    public Bag(int maxNumberOfKeys) {
        this.maxsize = maxNumberOfKeys;
        map = new LinkedHashMap<K, List<V>>() {
            protected boolean removeEldestEntry(Map.Entry<K, List<V>> eldestEntry) {
                return size() > maxsize;
            }
        };
    }

    @Override
    public int size() {
        int size = 0;
        for (K keys : map.keySet()) {
            ArrayList values = (ArrayList) map.get(keys);
            size = size + values.size();
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    @Override
    public V get(Object key) {
        List<V> values = map.get(key);
        if (values != null && !values.isEmpty()) {
            return values.get(0);
        } else {
            return null;
        }
    }

    @Override
    public V put(K key, V value) {
        List<V> values;
        if (map.containsKey(key)) {
            values = map.get(key);
            values.add(value);
        } else {
            values = new ArrayList<V>();
            values.add(value);
        }
        map.put(key, values);
        return value;
    }

    public Collection<V> put(K key, Collection<V> value) {
        List<V> values;

        if (map.containsKey(key)) {
            values = map.get(key);
            values.addAll(value);
        } else {
            values = new ArrayList<V>();
            values.addAll(value);
        }

        map.put(key, values);

        return values;
    }

    @Override
    public V remove(Object key) {
        return (V) map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        for (K k : map.keySet()) {
            this.put(k, map.get(k));
        }
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        List<V> values = new ArrayList<V>();

        for (K key : map.keySet()) {
            List<V> keyValues = map.get(key);
            values.addAll(keyValues);
        }
        return values;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> set = new HashSet<Entry<K, V>>();
        for (final K key : this.keySet()) {
            set.add(new Entry<K, V>() {
                public K getKey() {
                    return key;
                }

                public V getValue() {
                    return Bag.this.get(key);
                }

                public V setValue(V value) {
                    throw new UnsupportedOperationException("Unsupported");
                }

            });
        }
        return set;
    }

    public List<V> getValues(K key) {
        if (map.containsKey(key)) {
            return map.get(key);
        } else {
            return new ArrayList<V>();
        }
    }
}
