package net.avh4.data.datum.store.service;

import java.io.IOException;

public interface Ref<T> {
    T commit(T store) throws IOException;

    T get();
}
