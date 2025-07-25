package de.kiliansen.lib.ObservableValue.map;

import de.kiliansen.lib.ObservableValue.base.IChangeDef;

public record ObservableMapChangeDef<T extends MapTypes<K, V>, K, V>(K key, V oldValue, V newValue,
                                                                     MapChangeType mapChangeType) implements IChangeDef<T> {
}
