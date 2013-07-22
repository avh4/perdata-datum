package net.avh4.data.per.service;

public interface Storage<S> {
    String fetchRef(String refName);

    void storeRef(String refName, String newContentKey);

    S fetch(String contentKey);

    boolean isStored(String contentKey);

    void store(String contentKey, S content);
}
