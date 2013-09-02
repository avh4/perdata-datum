package net.avh4.data.datum.prim;

import net.avh4.data.datum.store.DatumStore;

public class ValueDatum implements Datum {
    private final Id entityId;
    private final String action;
    private final String value;

    public ValueDatum(Id entityId, String action, String value) {
        this.entityId = checkNotNull(entityId);
        this.action = checkNotNull(action);
        this.value = checkNotNull(value);
    }

    private static <T> T checkNotNull(T value) {
        if (value == null) throw new NullPointerException();
        return value;
    }

    @Override public String entityId() {
        return entityId.id();
    }

    @Override public String action() {
        return action;
    }

    @Override public String value() {
        return value;
    }

    @Override public DatumStore resolveTempIds(DatumStore store) {
        return entityId.resolve(store);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValueDatum datum = (ValueDatum) o;

        if (!action.equals(datum.action)) return false;
        if (!entityId.equals(datum.entityId)) return false;
        if (!value.equals(datum.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = entityId.hashCode();
        result = 31 * result + action.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override public String toString() {
        return "{" +
                entityId + ':' +
                action + ':' +
                value +
                '}';
    }
}
