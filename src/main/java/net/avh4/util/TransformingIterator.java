package net.avh4.util;

import fj.F;

import java.util.Iterator;

public class TransformingIterator<A, B> implements Iterator<B> {
    private final Iterator<A> itr;
    private final F<A, B> f;

    public TransformingIterator(Iterator<A> itr, F<A, B> f) {
        this.itr = itr;
        this.f = f;
    }

    @Override public boolean hasNext() {
        return itr.hasNext();
    }

    @Override public B next() {
        return f.f(itr.next());
    }

    @Override public void remove() {
        throw new IllegalStateException("TransformingIterator is read-only");
    }
}
