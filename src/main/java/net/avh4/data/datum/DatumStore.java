package net.avh4.data.datum;

public interface DatumStore {
    void write(String entity, String action, String value);

    String get(String entityId, String title);
}
