package net.avh4.data.datum.transact.commands;

import net.avh4.data.datum.primitives.Id;
import net.avh4.data.datum.store.DatumStore;
import net.avh4.data.datum.transact.Command;
import net.avh4.data.datum.transact.TransactionException;

public class Set implements Command {
    private final Id entity;
    private final String action;
    private final String value;

    public Set(Id entity, String action, String value) {
        this.entity = entity;
        this.action = action;
        this.value = value;
    }

    @Override public DatumStore execute(DatumStore store) throws TransactionException {
        final String entityId = this.entity.id();
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
        return store;
    }

    @Override public String toString() {
        return "Set(" + entity + ", " + action + ", " + value + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Set set = (Set) o;

        if (action != null ? !action.equals(set.action) : set.action != null) return false;
        if (entity != null ? !entity.equals(set.entity) : set.entity != null) return false;
        if (value != null ? !value.equals(set.value) : set.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = entity != null ? entity.hashCode() : 0;
        result = 31 * result + (action != null ? action.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
