package de.kiliansen.lib.ObservableValue.list;

import de.kiliansen.lib.ObservableValue.base.IChangeDef;

public record ObservableListChangeDef<T>(
        int index,
        T oldValue,
        T newValue,
        ListChangeType listChangeType
) implements IChangeDef<T> {

    public enum ListChangeType {
        ADD,
        REMOVE,
        UPDATE,
        CLEAR
    }
}
