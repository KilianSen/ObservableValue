package de.kiliansen.lib.ObservableValue.list;

import de.kiliansen.lib.ObservableValue.base.BaseObservable;
import de.kiliansen.lib.ObservableValue.base.ThreadedListener;
import de.kiliansen.lib.ObservableValue.map.ObservableMap;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class ObservableList<T> extends BaseObservable<T, ObservableListChangeDef<T>> implements List<T>, IListChanges<T> {
    private final List<T> values;

    /**
     * Constructs an ObservableValue with a null initial value.
     */
    public ObservableList() {
        setNamingStrategy(threadInfo ->
                ObservableList.class.getSimpleName() + "-Thread-" + threadInfo.id());
        this.values = new CopyOnWriteArrayList<>();
    }

    @Override
    public ThreadedListener<T, ObservableListChangeDef<T>> onChange(Consumer<ObservableListChangeDef<T>> listener, boolean useSameThread) {
        return onChangeDef(listener, useSameThread);
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return values.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return values.iterator();
    }

    @Override
    public Object[] toArray() {
        return values.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return values.toArray(a);
    }

    @Override
    public boolean add(T t) {
        values.add(t);
        var index = values.lastIndexOf(t);

        T oldValue = null;
        if (index > 0 && index < values.size()) {
            oldValue = values.get(index - 1);
        }

        trigger(new ObservableListChangeDef<>(index, oldValue, t, ObservableListChangeDef.ListChangeType.ADD));
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = values.indexOf(o);
        if (index >= 0) {
            var ret = values.remove(o);
            if (!ret) return false;

            T newValue = null;
            if (index < values.size()) {
                newValue = values.get(index);
            }

            trigger(new ObservableListChangeDef<>(index, (T) o, newValue, ObservableListChangeDef.ListChangeType.REMOVE));
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return new HashSet<>(values).containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean r = values.addAll(c);
        if (r) {
            for (T item : c) {
                trigger(new ObservableListChangeDef<>(values.lastIndexOf(item), null, item, ObservableListChangeDef.ListChangeType.ADD));
            }
        }
        return r;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        boolean r = values.addAll(index, c);
        var a = c.stream().toList();
        if (r) {
            for (int i = 0; i < a.size(); i++) {
                var item = a.get(i);
                int actualIndex = index + i;
                trigger(new ObservableListChangeDef<>(actualIndex, null, item, ObservableListChangeDef.ListChangeType.ADD));
            }
        }
        return r;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object item : c) {
            int index = values.indexOf(item);
            if (index >= 0) {
                values.remove(index);
                trigger(new ObservableListChangeDef<>(index, (T) item, null, ObservableListChangeDef.ListChangeType.REMOVE));
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        if (values.isEmpty()) return;
        values.clear();
        trigger(new ObservableListChangeDef<>(0, null, null, ObservableListChangeDef.ListChangeType.CLEAR));
    }

    @Override
    public T get(int index) {
        return values.get(index);
    }

    @Override
    public T set(int index, T element) {
        T oldValue = values.get(index);
        values.set(index, element);
        trigger(new ObservableListChangeDef<>(index, oldValue, element, ObservableListChangeDef.ListChangeType.UPDATE));
        return oldValue;
    }

    @Override
    public void add(int index, T element) {
        T oldValue = null;
        if (index < values.size() && index >= 0) {
            oldValue = values.get(index);
        }
        values.add(index, element);
        trigger(new ObservableListChangeDef<>(index, oldValue, element, ObservableListChangeDef.ListChangeType.ADD));
    }

    @Override
    public T remove(int index) {
        T oldValue = values.remove(index);
        T newValue = null;
        if (index < values.size()) {
            newValue = values.get(index);
        }
        trigger(new ObservableListChangeDef<>(index, oldValue, newValue, ObservableListChangeDef.ListChangeType.REMOVE));
        return oldValue;
    }

    @Override
    public int indexOf(Object o) {
        return values.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return values.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return values.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return values.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return values.subList(fromIndex, toIndex);
    }
}