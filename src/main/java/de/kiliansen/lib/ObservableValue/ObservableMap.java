package de.kiliansen.lib.ObservableValue;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ObservableMap<K, V> extends BaseObservableValue<Map<K, V>> implements Map<K, V> {
    private final Map<K, V> delegate;

    public ObservableMap() {
        this.delegate = new HashMap<>();
        super.set(delegate);
    }

    public ObservableMap(Map<K, V> initial) {
        this.delegate = Objects.requireNonNull(initial);
        super.set(delegate);
    }

    private void notifyChange() {
        super.set(delegate, true);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return delegate.get(key);
    }

    @Override
    public V put(K key, V value) {
        V old = delegate.put(key, value);
        notifyChange();
        return old;
    }

    @Override
    public V remove(Object key) {
        V old = delegate.remove(key);
        notifyChange();
        return old;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        delegate.putAll(m);
        notifyChange();
    }

    @Override
    public void clear() {
        delegate.clear();
        notifyChange();
    }

    @Override
    public Set<K> keySet() {
        return delegate.keySet();
    }

    @Override
    public Collection<V> values() {
        return delegate.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return delegate.entrySet();
    }

    // Optional override for default methods that mutate
    @Override
    public V putIfAbsent(K key, V value) {
        V old = delegate.putIfAbsent(key, value);
        if (!Objects.equals(old, value)) {
            notifyChange();
        }
        return old;
    }

    @Override
    public boolean remove(Object key, Object value) {
        boolean removed = delegate.remove(key, value);
        if (removed) notifyChange();
        return removed;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        boolean replaced = delegate.replace(key, oldValue, newValue);
        if (replaced) notifyChange();
        return replaced;
    }

    @Override
    public V replace(K key, V value) {
        V old = delegate.replace(key, value);
        if (old != null || delegate.containsKey(key)) {
            notifyChange();
        }
        return old;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        delegate.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        delegate.replaceAll(function);
        notifyChange();
    }
}