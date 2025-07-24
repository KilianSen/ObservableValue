package de.lmu.roborally.client.lib.ObservableValue;

import java.util.function.BiConsumer;

public interface IOVDiffable<T> extends IObservableValue<T> {
    AutoCloseable onChange(BiConsumer<T, T> listener, boolean useSameThread);

    /**
     * Registers a listener that will be called when the value changes.
     * @param listener the listener to register
     * @param on the value to listen for changes to
     *
     * @return an AutoCloseable that can be used to unregister the listener
     */
    default AutoCloseable onChange(Runnable listener, T on) {
        return onChange(listener, on, false);
    }

    /**
     * Registers a listener that will be called when the value changes to the specified value.
     * @param listener the listener to register
     * @param on the value to listen for changes to
     * @param useSameThread if true, the listener will be called in the same thread that calls set()
     *
     * @return an AutoCloseable that can be used to unregister the listener
     */
    default AutoCloseable onChange(Runnable listener, T on, boolean useSameThread) {
        return onChange(listener, on, useSameThread, false);
    }

    /**
     * Registers a listener that will be called when the value changes to the specified value.
     * @param listener the listener to register
     * @param on the value to listen for changes to
     * @param useSameThread if true, the listener will be called in the same thread that calls set()
     * @param triggerOnSameValue if true, the listener will be triggered even if the new value is the same as the old value
     *
     * @return an AutoCloseable that can be used to unregister the listener
     */
    default AutoCloseable onChange(Runnable listener, T on, boolean useSameThread, boolean triggerOnSameValue) {
        return onChange((o,n ) -> {
            if (on.equals(n) && (!n.equals(o) || triggerOnSameValue)) {
                listener.run();
            }
        }, useSameThread);
    }
}
