package net.avh4.data.per.service;

import java.util.HashMap;

public class InMemoryStorage<S> implements Storage<S> {
    private final HashMap<String, String> refs = new HashMap<>();
    private final HashMap<String, S> store = new HashMap<>();

    @Override
    public String fetchRef(String refName) {
        return refs.get(refName);
    }

    @Override
    public S fetch(String contentKey) {
        return store.get(contentKey);
    }

    @Override
    public boolean isStored(String contentKey) {
        return store.containsKey(contentKey);
    }

    @Override
    public void store(String contentKey, S content) {
        store.put(contentKey, content);
    }

    @Override
    public void storeRef(String refName, String newContentKey) {
        refs.put(refName, newContentKey);
    }
}
