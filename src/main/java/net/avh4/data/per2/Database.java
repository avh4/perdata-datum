package net.avh4.data.per2;

public interface Database {
    <T> T transact(Transaction<T> transaction);

    <T> T[] query(Class<T> documentClass);

    <T> T get(Class<T> documentClass, String entityId);
}
