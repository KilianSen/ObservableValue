package de.kiliansen.lib.ObservableValue.value;

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
    default void set(T value) {
        set(value, false);
    }

    /**
     * Gets the current value of this observable.
     *
     * @return the current value
     */
    T get();


}

