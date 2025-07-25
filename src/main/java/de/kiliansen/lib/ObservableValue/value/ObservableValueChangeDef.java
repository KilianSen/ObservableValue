package de.kiliansen.lib.ObservableValue.value;

import de.kiliansen.lib.ObservableValue.base.IChangeDef;

public record ObservableValueChangeDef<T>(T oldValue, T newValue) implements IChangeDef<T> {
}
