package de.kiliansen.lib.ObservableValue;

import java.util.*;

public class ObservableList<E> extends BaseObservableValue<List<E>> implements List<E> {
    private final List<E> delegate;

    public ObservableList() {
        this.delegate = new ArrayList<>();
        super.set(delegate);
    }

    public ObservableList(List<E> initial) {
        this.delegate = Objects.requireNonNull(initial);
        super.set(delegate);
    }

    private void notifyChange() {
        super.set(delegate, true);
    }

    @Override
    public boolean add(E e) {
        delegate.add(e);
        notifyChange();
        return true;
    }

    @Override
    public void add(int index, E element) {
        delegate.add(index, element);
        notifyChange();
    }

    @Override
    public E remove(int index) {
        E old = delegate.remove(index);
        notifyChange();
        return old;
    }

    @Override
    public boolean remove(Object o) {
        boolean changed = delegate.remove(o);
        if (changed) notifyChange();
        return changed;
    }

    @Override
    public E get(int index) {
        return delegate.get(index);
    }

    @Override
    public E getFirst() {
        return delegate.getFirst();
    }

    @Override
    public E getLast() {
        return delegate.getLast();
    }

    @Override
    public E set(int index, E element) {
        E old = delegate.set(index, element);
        notifyChange();
        return old;
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return delegate.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return delegate.iterator();
    }

    @Override
    public Object[] toArray() {
        return delegate.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return delegate.toArray(a);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return new HashSet<>(delegate).containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = delegate.addAll(c);
        if (changed) notifyChange();
        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean changed = delegate.addAll(index, c);
        if (changed) notifyChange();
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = delegate.removeAll(c);
        if (changed) notifyChange();
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = delegate.retainAll(c);
        if (changed) notifyChange();
        return changed;
    }

    @Override
    public void clear() {
        delegate.clear();
        notifyChange();
    }

    @Override
    public int indexOf(Object o) {
        return delegate.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return delegate.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return delegate.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return delegate.listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return delegate.subList(fromIndex, toIndex);
    }
}