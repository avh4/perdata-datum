package net.avh4.data.per2;

public interface DatumStore {
    void write(String entity, String action, String value);

    String get(String entityId, String title);
}
