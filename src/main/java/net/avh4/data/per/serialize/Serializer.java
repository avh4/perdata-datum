package net.avh4.data.per.serialize;

public interface Serializer<T> {
    byte[] serializeToArray(T object);

    T deserialize(byte[] bytes);
}
