package net.avh4.data.per.service;

import java.util.HashMap;

public class InMemoryStorage implements Storage {
    private final HashMap<String, String> refs = new HashMap<>();
    private final HashMap<String, byte[]> store = new HashMap<>();

    @Override
    public String fetchRef(String refName) {
        return refs.get(refName);
    }

    @Override
    public byte[] fetchBytes(String contentKey) {
        return store.get(contentKey);
    }

    @Override
    public boolean hasBytes(String contentKey) {
        return store.containsKey(contentKey);
    }

    @Override
    public void storeBytes(String contentKey, byte[] bytes) {
        store.put(contentKey, bytes);
    }

    @Override
    public void storeRef(String refName, String newContentKey) {
        refs.put(refName, newContentKey);
    }
}
