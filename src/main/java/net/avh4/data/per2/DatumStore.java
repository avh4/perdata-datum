package net.avh4.data.per2;

public interface DatumStore {
    void write(EntityId entity, String action, String value);

    String get(EntityId entityId, String title);
}
