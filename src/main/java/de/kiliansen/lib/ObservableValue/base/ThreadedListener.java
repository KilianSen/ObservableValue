package de.kiliansen.lib.ObservableValue.base;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * A class that wraps a listener and a flag indicating whether the listener should be called
 * in the same thread that triggers the observable value change.
 */
public final class ThreadedListener<T, F extends IChangeDef<T>> {
    private final Consumer<F> listener;
    private final AtomicBoolean useSameThread;

    private final IObservable<T, F> value;

    public ThreadedListener(Consumer<F> listener, boolean useSameThread, IObservable<T, F> value) throws IllegalArgumentException {
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("Close callback cannot be null");
        }

        this.listener = listener;
        this.useSameThread = new AtomicBoolean(useSameThread);
        this.value = value;
    }

    public Consumer<F> listener() {
        return listener;
    }

    public boolean useSameThread() {
        return useSameThread.get();
    }

    public void setUseSameThread(boolean useSameThread) {
        this.useSameThread.set(useSameThread);
    }

    public void close() {
        value.removeListener(listener);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ThreadedListener other) {
            return Objects.equals(this.listener, other.listener) &&
                    this.useSameThread == other.useSameThread;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(listener, useSameThread);
    }

    @Override
    public String toString() {
        return "ThreadedListener[" +
                "listener=" + listener + ", " +
                "useSameThread=" + useSameThread + ']';
    }
}
