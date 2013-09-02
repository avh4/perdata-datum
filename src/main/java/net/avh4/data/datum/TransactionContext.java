package net.avh4.data.datum;

public interface TransactionContext {
    String create();

    Void set(String entityId, String action, Object value);

    Void add(String entityId, String action, Object value);
}
