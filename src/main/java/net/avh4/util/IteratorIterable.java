package net.avh4.util;

import java.util.Iterator;

public class IteratorIterable<T> implements Iterable<T> {
    private final Iterator<T> itr;

    public IteratorIterable(Iterator<T> itr) {
        this.itr = itr;
    }

    @Override public Iterator<T> iterator() {
        return itr;
    }
}
