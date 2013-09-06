package net.avh4.data.datum.store.service;

import java.io.IOException;

public interface Serializer<T> {
    void writeToDisk(T value) throws IOException;

    T readLatest() throws IOException;
}
