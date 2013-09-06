package net.avh4.data.datum.store.service;

import java.io.IOException;
import java.io.Serializable;

public class SynchronouslySerializedRef<T extends Serializable> extends InMemoryRef<T> {
    private final Serializer<T> serializer;

    public SynchronouslySerializedRef(Serializer<T> serializer) throws IOException {
        super(serializer.readLatest());
        this.serializer = serializer;
    }

    @Override public T commit(T newValue) throws IOException {
        serializer.writeToDisk(newValue);
        return super.commit(newValue);
    }
}
