package de.kiliansen.lib.ObservableValue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class BaseObservableValue<T> implements IObservableValue<T> {
    /**
     * ExecutorService that manages the threads for notifying listeners.
     * This uses a fixed thread pool with a number of threads equal to half the number of available processors,
     * ensuring that there are always threads available for listener notifications.
     * This is designed to be efficient and avoid creating too many threads,
     * especially in scenarios where many listeners might be registered.
     * * The threads are set as daemon threads, meaning they will not prevent the JVM from exiting
     * if they are the only threads running.
     */
    private static final ExecutorService executor =
            Executors.newFixedThreadPool(Math.max(1, Runtime.getRuntime().availableProcessors()),
                    (Runnable r) -> {
                        Thread t = new Thread(r);
                        t.setDaemon(true);
                        t.setName(ObservableValue.class.getSimpleName() + "-Thread-" + t.threadId());
                        return t;
                    }
            );

    private static final Logger logger = LogManager.getLogger(BaseObservableValue.class);

    private final List<ThreadedListener<T>> listeners = new CopyOnWriteArrayList<>();
    private final AtomicReference<T> value = new AtomicReference<>();

    /**
     * Default constructor that initializes the observable value to null.
     */
    public BaseObservableValue() {
        this.value.set(null);
    }

    /**
     * {@link ObservableValue} constructor that initializes the observable value with a given initial value.
     *
     * @param initialValue the initial value to set for this observable
     */
    public BaseObservableValue(T initialValue) {
        this.value.set(initialValue);
    }

    /**
     * Sets a new value for this observable. If the new value is the same as the current value and
     *
     * @param newValue    the new value to set for this observable
     * @param forceNotify if true, forces notification to listeners even if the value has not changed
     */
    @Override
    public void set(T newValue, boolean forceNotify) {
        if (!forceNotify && Objects.equals(value.get(), newValue)) {
            return;
        }
        value.set(newValue);

        trigger(newValue);
    }

    public void trigger() {
        trigger(value.get());
    }

    public void trigger(T newValue) {
        listeners.parallelStream().forEach(tl -> {
            if (tl.useSameThread) {
                //logger.debug("Notifying listener in the same thread: {}", Thread.currentThread().getName());
                tl.listener.accept(newValue);
            } else {
                executor.submit(() -> {
                    //logger.debug("Notifying listener in thread: {}", Thread.currentThread().getName());
                    tl.listener.accept(newValue);
                });
            }
        });
    }

    /**
     * Removes all listeners from this observable.
     * This method clears the list of listeners and logs the action.
     */
    @Override
    public void removeAllListeners() {
        listeners.clear();
        logger.debug("Cleared all listeners for observable value.");
    }

    /**
     * Removes a specific listener from this observable.
     *
     * @param closeable the AutoCloseable listener to remove
     */
    @Override
    public void removeListener(AutoCloseable closeable) {
        try {
            closeable.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes a specific listener from this observable.
     *
     * @param listener the listener to remove
     */
    @Override
    public void removeListener(Consumer<T> listener) {
        listeners.removeIf(tl -> tl.listener.equals(listener));
    }

    /**
     * Sets a new value for this observable.
     *
     * @param value the new value to set for this observable
     */
    @Override
    public void set(T value) {
        set(value, false);
    }

    /**
     * Gets the current value of this observable.
     *
     * @return the current value of this observable
     */
    @Override
    public T get() {
        return value.get();
    }

    /**
     * Registers a listener that will be notified when the value of this observable changes.
     *
     * @param listener      the listener to register
     * @param useSameThread if true, the listener will be called in the same thread that calls set()
     *
     * @return an AutoCloseable that can be used to unregister the listener
     */
    @Override
    public AutoCloseable onChange(Consumer<T> listener, boolean useSameThread) {
        ThreadedListener<T> tl = new ThreadedListener<>(listener, useSameThread);
        listeners.add(tl);
        return () -> listeners.remove(tl);
    }

    /**
     * Records a listener that will be notified when the value of this observable changes.
     */
    private record ThreadedListener<T>(Consumer<T> listener, boolean useSameThread) {
    }
}
