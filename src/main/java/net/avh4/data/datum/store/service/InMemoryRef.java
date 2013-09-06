package net.avh4.data.datum.store.service;

import java.io.IOException;

public class InMemoryRef<T> implements Ref<T> {
    private T value;

    public InMemoryRef(T initial) {
        this.value = initial;
    }

    @Override public T commit(T newValue) throws IOException {
        value = newValue;
        return newValue;
    }

    @Override public T get() {
        return value;
    }
}
