package de.kiliansen.lib.ObservableValue.map;

import de.kiliansen.lib.ObservableValue.base.ThreadedListener;
import de.kiliansen.lib.ObservableValue.util.TriConsumer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface IMapChanges<K, V> {
    ThreadedListener<MapTypes<K, V>, ObservableMapChangeDef<MapTypes<K, V>, K, V>> onChange(Consumer<ObservableMapChangeDef<MapTypes<K, V>, K, V>> listener, boolean useSameThread);

    default ThreadedListener<MapTypes<K, V>, ObservableMapChangeDef<MapTypes<K, V>, K, V>> onPut(TriConsumer<K, V, V> listener, boolean useSameThread) {
        return onChange(change -> {
            if (change.mapChangeType() == MapChangeType.PUT) {
                listener.accept(change.key(), change.oldValue(), change.newValue());
            }
        }, useSameThread);
    }

    default ThreadedListener<MapTypes<K, V>, ObservableMapChangeDef<MapTypes<K, V>, K, V>> onPut(BiConsumer<K, V> listener, boolean useSameThread) {
        return onPut(
                (k, oldValue, newValue) -> listener.accept(k, newValue), useSameThread);
    }

    default ThreadedListener<MapTypes<K, V>, ObservableMapChangeDef<MapTypes<K, V>, K, V>> onPut(TriConsumer<K, V, V> listener) {
        return onPut(listener, false);
    }

    default ThreadedListener<MapTypes<K, V>, ObservableMapChangeDef<MapTypes<K, V>, K, V>> onPut(BiConsumer<K, V> listener) {
        return onPut(listener, false);
    }

    default ThreadedListener<MapTypes<K, V>, ObservableMapChangeDef<MapTypes<K, V>, K, V>> onRemove(TriConsumer<K, V, V> listener, boolean useSameThread) {
        return onChange(change -> {
            if (change.mapChangeType() == MapChangeType.REMOVE) {
                listener.accept(change.key(), change.oldValue(), change.newValue());
            }
        }, useSameThread);
    }

    default ThreadedListener<MapTypes<K, V>, ObservableMapChangeDef<MapTypes<K, V>, K, V>> onRemove(BiConsumer<K, V> listener, boolean useSameThread) {
        return onRemove(
                (k, oldValue, newValue) -> listener.accept(k, oldValue), useSameThread);
    }

    default ThreadedListener<MapTypes<K, V>, ObservableMapChangeDef<MapTypes<K, V>, K, V>> onRemove(TriConsumer<K, V, V> listener) {
        return onRemove(listener, false);
    }

    default ThreadedListener<MapTypes<K, V>, ObservableMapChangeDef<MapTypes<K, V>, K, V>> onRemove(BiConsumer<K, V> listener) {
        return onRemove(listener, false);
    }

    default ThreadedListener<MapTypes<K, V>, ObservableMapChangeDef<MapTypes<K, V>, K, V>> onClear(Runnable listener, boolean useSameThread) {
        return onChange(change -> {
            if (change.mapChangeType() == MapChangeType.CLEAR) {
                listener.run();
            }
        }, useSameThread);
    }

    default ThreadedListener<MapTypes<K, V>, ObservableMapChangeDef<MapTypes<K, V>, K, V>> onClear(Runnable listener) {
        return onClear(listener, false);
    }
}
