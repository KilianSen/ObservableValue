package de.kiliansen.lib.ObservableValue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ObservableValue<T> extends BaseObservableValue<T> implements IOVDiffable<T> {
    private static final Logger logger = LogManager.getLogger(ObservableValue.class);
    private final AtomicReference<T> previousValue = new AtomicReference<>();

    public ObservableValue(T value) {
        super(value);
    }

    public ObservableValue() {
        super();
    }

    @Override
    public AutoCloseable onChange(BiConsumer<T, T> listener, boolean useSameThread) {
        return onChange(value -> {
            T oldValue = previousValue.getAndSet(value);
            if (oldValue != null) {
                listener.accept(oldValue, value);
            } else {
                listener.accept(value, value);
            }
            previousValue.set(value);
        }, useSameThread);
    }
}
