package net.avh4.data.datum.prim;

import net.avh4.data.datum.store.DatumStore;

public class RefDatum implements Datum {
    private final Id entityId;
    private final String action;
    private final Id refId;

    public RefDatum(Id entityId, String action, Id refId) {
        this.entityId = entityId;
        this.action = action;
        this.refId = refId;
    }

    @Override public String entityId() {
        return entityId.id();
    }

    @Override public String action() {
        return action;
    }

    @Override public String value() {
        return refId.id();
    }

    @Override public DatumStore resolveTempIds(DatumStore store) {
        store  = entityId.resolve(store);
        store = refId.resolve(store);
        return store;
    }

    @Override public String toString() {
        return "{" +
                entityId + ':' +
                action + ':' +
                refId +
                '}';
    }
}
