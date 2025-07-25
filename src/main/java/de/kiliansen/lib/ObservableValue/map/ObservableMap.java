package de.kiliansen.lib.ObservableValue.map;

import de.kiliansen.lib.ObservableValue.base.BaseObservable;
import de.kiliansen.lib.ObservableValue.base.ThreadedListener;
import de.kiliansen.lib.ObservableValue.value.ObservableValue;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ObservableMap<K, V> extends BaseObservable<MapTypes<K, V>, ObservableMapChangeDef<MapTypes<K, V>, K, V>> implements Map<K, V>, IMapChanges<K, V> {
    private final Map<K, V> delegate;

    public ObservableMap() {
        setNamingStrategy(threadInfo ->
                ObservableMap.class.getSimpleName() + "-Thread-" + threadInfo.id());
        this.delegate = new ConcurrentHashMap<>();
    }

    public ObservableMap(Map<K, V> initialMap) {
        setNamingStrategy(threadInfo ->
                ObservableMap.class.getSimpleName() + "-Thread-" + threadInfo.id());
        this.delegate = new ConcurrentHashMap<>(initialMap);
    }

    public ThreadedListener<MapTypes<K, V>, ObservableMapChangeDef<MapTypes<K, V>, K, V>> onChange(Consumer<ObservableMapChangeDef<MapTypes<K, V>, K, V>> listener, boolean useSameThread) {
        return super.onChangeDef(listener, useSameThread);
    }

    @Override
    public V put(Object key, Object value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key and value must match the map's type parameters.");
        }
        K k = (K) key;
        V v = (V) value;
        V oldValue = delegate.put(k, v);
        trigger(new ObservableMapChangeDef<>(k, oldValue, v, MapChangeType.PUT));
        return oldValue;
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
    public V remove(Object key) {
        V oldValue = delegate.remove(key);
        if (oldValue != null) {
            // Trigger a change event only if the key existed
            trigger(new ObservableMapChangeDef<>((K) key, oldValue, null, MapChangeType.REMOVE));
        }
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            V oldValue = delegate.put(key, value);
            trigger(new ObservableMapChangeDef<>(key, oldValue, value, MapChangeType.PUT));
        }
    }

    @Override
    public void clear() {
        delegate.clear();
        trigger(new ObservableMapChangeDef<>(null, null, null, MapChangeType.CLEAR));
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
}