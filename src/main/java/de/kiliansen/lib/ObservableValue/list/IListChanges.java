package de.kiliansen.lib.ObservableValue.list;

import de.kiliansen.lib.ObservableValue.base.ThreadedListener;
import de.kiliansen.lib.ObservableValue.util.TriConsumer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface IListChanges<T> {
    ThreadedListener<T, ObservableListChangeDef<T>> onChange(Consumer<ObservableListChangeDef<T>> listener, boolean useSameThread);

    default ThreadedListener<T, ObservableListChangeDef<T>> onAdd(TriConsumer<T, T, Integer> listener, boolean useSameThread) {
        return onChange(change -> {
            if (change.listChangeType() == ObservableListChangeDef.ListChangeType.ADD) {
                listener.accept(change.oldValue(), change.newValue(), change.index());
            }
        }, useSameThread);
    }

    default ThreadedListener<T, ObservableListChangeDef<T>> onAdd(BiConsumer<T, Integer> listener, boolean useSameThread) {
        return onChange(change -> {
            if (change.listChangeType() == ObservableListChangeDef.ListChangeType.ADD) {
                listener.accept(change.newValue(), change.index());
            }
        }, useSameThread);
    }

    default ThreadedListener<T, ObservableListChangeDef<T>> onAdd(TriConsumer<T, T, Integer> listener) {
        return onAdd(listener, false);
    }

    default ThreadedListener<T, ObservableListChangeDef<T>> onAdd(BiConsumer<T, Integer> listener) {
        return onAdd(listener, false);
    }

    default ThreadedListener<T, ObservableListChangeDef<T>> onRemove(TriConsumer<T, T, Integer> listener, boolean useSameThread) {
        return onChange(change -> {
            if (change.listChangeType() == ObservableListChangeDef.ListChangeType.REMOVE) {
                listener.accept(change.oldValue(), change.newValue(), change.index());
            }
        }, useSameThread);
    }

    default ThreadedListener<T, ObservableListChangeDef<T>> onRemove(BiConsumer<T, Integer> listener, boolean useSameThread) {
        return onChange(change -> {
            if (change.listChangeType() == ObservableListChangeDef.ListChangeType.REMOVE) {
                listener.accept(change.oldValue(), change.index());
            }
        }, useSameThread);
    }

    default ThreadedListener<T, ObservableListChangeDef<T>> onRemove(TriConsumer<T, T, Integer> listener) {
        return onRemove(listener, false);
    }

    default ThreadedListener<T, ObservableListChangeDef<T>> onRemove(BiConsumer<T, Integer> listener) {
        return onRemove(listener, false);
    }

    default ThreadedListener<T, ObservableListChangeDef<T>> onUpdate(TriConsumer<T, T, Integer> listener, boolean useSameThread) {
        return onChange(change -> {
            if (change.listChangeType() == ObservableListChangeDef.ListChangeType.UPDATE) {
                listener.accept(change.oldValue(), change.newValue(), change.index());
            }
        }, useSameThread);
    }

    default ThreadedListener<T, ObservableListChangeDef<T>> onUpdate(BiConsumer<T, Integer> listener, boolean useSameThread) {
        return onChange(change -> {
            if (change.listChangeType() == ObservableListChangeDef.ListChangeType.UPDATE) {
                listener.accept(change.newValue(), change.index());
            }
        }, useSameThread);
    }

    default ThreadedListener<T, ObservableListChangeDef<T>> onUpdate(TriConsumer<T, T, Integer> listener) {
        return onUpdate(listener, false);
    }

    default ThreadedListener<T, ObservableListChangeDef<T>> onUpdate(BiConsumer<T, Integer> listener) {
        return onUpdate(listener, false);
    }

    default ThreadedListener<T, ObservableListChangeDef<T>> onClear(Runnable listener, boolean useSameThread) {
        return onChange(change -> {
            if (change.listChangeType() == ObservableListChangeDef.ListChangeType.CLEAR) {
                listener.run();
            }
        }, useSameThread);
    }

    default ThreadedListener<T, ObservableListChangeDef<T>> onClear(Runnable listener) {
        return onClear(listener, false);
    }
}
