package de.kiliansen.lib.ObservableValue.base;

public interface ITriggerable<T, F extends IChangeDef<T>> {
    void trigger(F changeDef);
}
