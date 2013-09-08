package net.avh4.data.datum.transact.commands;

import net.avh4.data.datum.primitives.Id;
import net.avh4.data.datum.store.DatumStore;
import net.avh4.data.datum.transact.Command;
import net.avh4.data.datum.transact.TransactionException;

public class Increment implements Command {
    private final Id entity;
    private final String action;

    public Increment(Id entity, String action) {
        this.entity = entity;
        this.action = action;
    }

    @Override public DatumStore execute(DatumStore store) throws TransactionException {
        final String entityId = entity.id();
        final String oldValue = store.get(entityId, action);
        int v = oldValue == null ? 0 : Integer.parseInt(oldValue);
        if (oldValue != null) {
            store = store.removeIndex(action, formatIndex(v), entityId);
        }
        v++;
        store = store.set(entityId, action, Integer.toString(v));
        store = store.addIndex(action, formatIndex(v), entityId);
        return store;
    }

    @Override public DatumStore resolveTempIds(DatumStore store) {
        store = entity.resolve(store);
        return store;
    }

    private String formatIndex(int v) {
        return String.format("%010d", v);
    }

    @Override public String toString() {
        return "Increment(" + entity + ", " + action + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Increment increment = (Increment) o;

        if (action != null ? !action.equals(increment.action) : increment.action != null) return false;
        if (entity != null ? !entity.equals(increment.entity) : increment.entity != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = entity != null ? entity.hashCode() : 0;
        result = 31 * result + (action != null ? action.hashCode() : 0);
        return result;
    }
}
