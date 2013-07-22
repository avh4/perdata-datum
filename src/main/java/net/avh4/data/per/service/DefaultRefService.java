package net.avh4.data.per.service;

import net.avh4.data.per.hash.Hasher;
import net.avh4.data.per.serialize.Serializer;

import static com.google.common.base.Preconditions.checkNotNull;

public class DefaultRefService<T, S> implements RefService<T> {
    private final Hasher<T> hasher;
    private final Serializer<T, S> serializer;
    private final Storage<S> storage;

    public DefaultRefService(Hasher<T> hasher, Serializer<T, S> serializer, Storage<S> storage) {
        this.hasher = hasher;
        this.serializer = serializer;
        this.storage = storage;
    }

    @Override
    public String getContentKey(String refName) {
        return storage.fetchRef(refName);
    }

    @Override
    public T getContent(String contentKey) {
        final S content = storage.fetch(contentKey);
        return serializer.deserialize(content);
    }

    @Override
    public String put(T object) {
        String key = hasher.hash(object);
        if (!storage.isStored(key)) {
            final S content = serializer.serializeToArray(object);
            storage.store(key, content);
        }
        return key;
    }

    @Override
    public void updateRef(String refName, String currentContentKey, String newContentKey) throws TransactionException {
        checkNotNull(refName);
        if (newContentKey != null && !storage.isStored(newContentKey)) {
            throw new RuntimeException("Content for " + newContentKey + " must be added with put before calling updateRef");
        }
        synchronized (this) {
            final String actualContentKey = storage.fetchRef(refName);
            if (currentContentKey == null && actualContentKey != null) {
                throw new TransactionException("Expected to create ref \"" + refName + "\" as " + newContentKey + ", but it already exists (with content key " + actualContentKey + ")");
            }
            if (currentContentKey != null && !currentContentKey.equals(actualContentKey)) {
                throw new TransactionException("Expected to update ref \"" + refName + "\" from " + currentContentKey + " to " + newContentKey + ", but it was " + actualContentKey);
            }
            storage.storeRef(refName, newContentKey);
        }
    }
}
