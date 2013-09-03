package net.avh4.data.datum.transact;

public interface KeyStore {
    void set(String key, String value);

    String get(String key);

    void remove(String key);
}
