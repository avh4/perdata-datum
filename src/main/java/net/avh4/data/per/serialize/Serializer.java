package net.avh4.data.per.serialize;

public interface Serializer<T, S> {
    S serializeToArray(T object);

    T deserialize(S serialized);
}
