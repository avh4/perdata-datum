package net.avh4.data.per.service;

public interface Storage {
    String fetchRef(String refName);

    void storeRef(String refName, String newContentKey);

    byte[] fetchBytes(String contentKey);

    boolean hasBytes(String contentKey);

    void storeBytes(String contentKey, byte[] bytes);
}
