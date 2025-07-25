package de.kiliansen.lib.ObservableValue.value;

import de.kiliansen.lib.ObservableValue.base.ThreadedListener;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface IDifferentiable<T> extends IObservableValue<T> {

    default ThreadedListener<T, ObservableValueChangeDef<T>> onChange(Consumer<T> listener, boolean useSameThread) {
        return onChange((oldValue, newValue) -> listener.accept(newValue), useSameThread);
    }

    default ThreadedListener<T, ObservableValueChangeDef<T>> onChange(Consumer<T> listener) {
        return onChange(listener, false);
    }

    ThreadedListener<T, ObservableValueChangeDef<T>> onChange(BiConsumer<T, T> listener, boolean useSameThread);
    default ThreadedListener<T, ObservableValueChangeDef<T>> onChange(BiConsumer<T, T> listener) {
        return onChange(listener, false);
    }

    /**
     * Registers a listener that will be called when the value changes.
     *
     * @param listener the listener to register
     * @param on       the value to listen for changes to
     *
     * @return an AutoCloseable that can be used to unregister the listener
     */
    default ThreadedListener<T, ObservableValueChangeDef<T>> onChange(Runnable listener, T on) {
        return onChange(listener, on, false);
    }

    /**
     * Registers a listener that will be called when the value changes to the specified value.
     *
     * @param listener      the listener to register
     * @param on            the value to listen for changes to
     * @param useSameThread if true, the listener will be called in the same thread that calls set()
     *
     * @return an AutoCloseable that can be used to unregister the listener
     */
    default ThreadedListener<T, ObservableValueChangeDef<T>> onChange(Runnable listener, T on, boolean useSameThread) {
        return onChange(listener, on, useSameThread, false);
    }

    /**
     * Registers a listener that will be called when the value changes to the specified value.
     *
     * @param listener           the listener to register
     * @param on                 the value to listen for changes to
     * @param useSameThread      if true, the listener will be called in the same thread that calls set()
     * @param triggerOnSameValue if true, the listener will be triggered even if the new value is the same as the old value
     *
     * @return an AutoCloseable that can be used to unregister the listener
     */
    default ThreadedListener<T, ObservableValueChangeDef<T>> onChange(Runnable listener, T on, boolean useSameThread, boolean triggerOnSameValue) {
        return onChange((o, n) -> {
            if (on.equals(n) && (!n.equals(o) || triggerOnSameValue)) {
                listener.run();
            }
        }, useSameThread);
    }
}
