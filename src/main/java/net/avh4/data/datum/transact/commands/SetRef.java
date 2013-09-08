package net.avh4.data.datum.transact.commands;

import net.avh4.data.datum.primitives.Id;
import net.avh4.data.datum.store.DatumStore;
import net.avh4.data.datum.transact.Command;
import net.avh4.data.datum.transact.TransactionException;

public class SetRef implements Command {
    private final Id entity;
    private final String action;
    private final Id ref;

    public SetRef(Id entity, String action, Id ref) {
        this.entity = entity;
        this.action = action;
        this.ref = ref;
    }

    @Override public DatumStore execute(DatumStore store) throws TransactionException {
        final String entityId = entity.id();
        final String value = ref.id();
        final String oldValue = store.get(entityId, action);
        store = store.set(entityId, action, value);
        if (oldValue != null) {
            store = store.removeIndex(action, oldValue, entityId);
        }
        store = store.addIndex(action, value, entityId);
        return store;
    }

    @Override public DatumStore resolveTempIds(DatumStore store) {
        store = entity.resolve(store);
        store = ref.resolve(store);
        return store;
    }


    @Override public String toString() {
        return "SetRef(" + entity + ", " + action + ", " + ref + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SetRef setRef = (SetRef) o;

        if (action != null ? !action.equals(setRef.action) : setRef.action != null) return false;
        if (entity != null ? !entity.equals(setRef.entity) : setRef.entity != null) return false;
        if (ref != null ? !ref.equals(setRef.ref) : setRef.ref != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = entity != null ? entity.hashCode() : 0;
        result = 31 * result + (action != null ? action.hashCode() : 0);
        result = 31 * result + (ref != null ? ref.hashCode() : 0);
        return result;
    }
}
