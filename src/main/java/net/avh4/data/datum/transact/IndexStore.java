package net.avh4.data.datum.transact;

public interface IndexStore {
    void add(String indexName, String value, String object);

    void remove(String indexName, String value, String object);
}
