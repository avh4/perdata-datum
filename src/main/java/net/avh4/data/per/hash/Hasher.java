package net.avh4.data.per.hash;

public interface Hasher<T> {
    String hash(T object);
}
