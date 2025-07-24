package de.lmu.roborally.client.lib.ObservableValue;

import java.util.function.Consumer;

public interface IObservableValue<T> {
    /**
     * Sets a new value for this observable. If the new value is the same as the current value and
     * forceNotify is false, no listeners will be notified.
     *
     * @param value       the new value to set for this observable
     * @param forceNotify if true, forces notification to listeners even if the value has not changed
     */
    void set(T value, boolean forceNotify);

    /**
     * Sets a new value for this observable. Listeners will be notified if the value changes.
     *
     * @param value the new value to set for this observable
     */
    void set(T value);

    /**
     * Gets the current value of this observable.
     *
     * @return the current value
     */
    T get();

    /**
     * Triggers the observable, notifying all listeners of a change.
     *
     * Note: This method should mostly be used internally in the implementation of the observable.
     */
    void trigger();

    /**
     * Triggers the observable with a new value, notifying all listeners of the change.
     *
     * @param newValue the new value to notify listeners about
     *
     * Note: The value doesnt have to be the set value nor the current value.
     * Similar to {@link #trigger()} this method should mostly be used internally in the implementation of the observable.
     */
    void trigger(T newValue);

    /**
     * Removes all listeners from this observable.
     */
    void removeAllListeners();

    /**
     * Removes a specific listener from this observable.
     *
     * @param closeable the AutoCloseable listener to remove
     */
    void removeListener(AutoCloseable closeable);

    void removeListener(Consumer<T> listener);

    AutoCloseable onChange(Consumer<T> listener, boolean useSameThread);

    default AutoCloseable onChange(Consumer<T> listener) {
        return onChange(listener, false);
    }
}

