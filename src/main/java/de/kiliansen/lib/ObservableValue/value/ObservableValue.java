package de.kiliansen.lib.ObservableValue.value;

import de.kiliansen.lib.ObservableValue.base.BaseObservable;
import de.kiliansen.lib.ObservableValue.base.Executor;
import de.kiliansen.lib.ObservableValue.base.ThreadedListener;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ObservableValue<T> extends BaseObservable<T, ObservableValueChangeDef<T>> implements IObservableValue<T>, IDifferentiable<T> {
    private final AtomicReference<T> value;

    /**
     * Constructs an ObservableValue with the specified initial value.
     *
     * @param initialValue the initial value of this observable
     */
    public ObservableValue(T initialValue) {
        setNamingStrategy(threadInfo ->
                ObservableValue.class.getSimpleName() + "-Thread-" + threadInfo.id());
        this.value = new AtomicReference<>(initialValue);
    }

    /**
     * Constructs an ObservableValue with a null initial value.
     */
    public ObservableValue() {
        setNamingStrategy(threadInfo ->
                ObservableValue.class.getSimpleName() + "-Thread-" + threadInfo.id());
        this.value = new AtomicReference<>(null);
    }

    /**
     * Sets a new value for this observable. If the new value is the same as the current value and
     *
     * @param newValue     the new value to set for this observable
     * @param forceTrigger if true, forces notification to listeners even if the value has not changed
     */
    @Override
    public void set(T newValue, boolean forceTrigger) {
        if (!forceTrigger && Objects.equals(value.get(), newValue)) return;
        trigger(new ObservableValueChangeDef<>(value.getAndSet(newValue), newValue));
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

    @Override
    public ThreadedListener<T, ObservableValueChangeDef<T>> onChange(BiConsumer<T, T> listener, boolean useSameThread) {
        Consumer<ObservableValueChangeDef<T>> changeListener;
        changeListener = changeDef -> listener.accept(changeDef.oldValue(), changeDef.newValue());
        return onChangeDef(changeListener, useSameThread);
    }
}