package de.kiliansen.lib.ObservableValue.base;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public abstract class BaseObservable<T, F extends IChangeDef<T>> extends Executor implements ITriggerable<T, F>, IObservable<T, F> {
    private final List<ThreadedListener<T, F>> listeners = new CopyOnWriteArrayList<>();

    @Override
    public void trigger(F changeDef) {
        listeners.parallelStream().forEach(tl -> {
            if (tl.useSameThread()) tl.listener().accept(changeDef);
            else getExecutor().submit(() -> tl.listener().accept(changeDef));
        });
    }

    /**
     * Removes all listeners from this observable.
     * This method clears the list of listeners and logs the action.
     */
    @Override
    public void removeAllListeners() {
        listeners.clear();
    }


    /**
     * Removes a specific listener from this observable.
     *
     * @param listener the listener to remove
     */
    @Override
    public void removeListener(Consumer<F> listener) {
        listeners.removeIf(tl -> tl.listener().equals(listener));
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
    public ThreadedListener<T, F> onChangeDef(Consumer<F> listener, boolean useSameThread) {
        ThreadedListener<T, F> tl = new ThreadedListener<>(listener, useSameThread, this);
        listeners.add(tl);
        return tl;
    }
}
