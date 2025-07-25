package de.kiliansen.lib.ObservableValue.base;

import java.util.function.Consumer;

public interface IObservable<T, F extends IChangeDef<T>> {
    /**
     * Removes all listeners from this observable.
     */
    void removeAllListeners();

    void removeListener(Consumer<F> listener);

    default void removeListener(ThreadedListener<T, F> listener) {
        if (listener == null) {
            // By default, a ThreadedListener cannot have a null listener.
            throw new RuntimeException("Listener cannot be null");
        }
        listener.close();
    }

    ThreadedListener<T, F> onChangeDef(Consumer<F> listener, boolean useSameThread);
}
