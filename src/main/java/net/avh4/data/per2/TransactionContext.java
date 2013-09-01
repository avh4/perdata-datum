package net.avh4.data.per2;

public interface TransactionContext {
    String create();

    Void set(String entityId, String action, Object value);

    Void add(String entityId, String action, Object value);
}
