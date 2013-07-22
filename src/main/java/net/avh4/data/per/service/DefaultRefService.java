package net.avh4.data.per.service;

import net.avh4.data.per.hash.Hasher;
import net.avh4.data.per.serialize.Serializer;

import static com.google.common.base.Preconditions.checkNotNull;

public class DefaultRefService<T> implements RefService<T> {
    private final Hasher<T> hasher;
    private final Serializer<T> serializer;
    private final Storage storage;

    public DefaultRefService(Hasher<T> hasher, Serializer<T> serializer, Storage storage) {
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
        final byte[] bytes = storage.fetchBytes(contentKey);
        return serializer.deserialize(bytes);
    }

    @Override
    public String put(T object) {
        String key = hasher.hash(object);
        if (!storage.hasBytes(key)) {
            final byte[] bytes = serializer.serializeToArray(object);
            storage.storeBytes(key, bytes);
        }
        return key;
    }

    @Override
    public void updateRef(String refName, String currentContentKey, String newContentKey) throws TransactionException {
        checkNotNull(refName);
        if (newContentKey != null && !storage.hasBytes(newContentKey)) {
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
