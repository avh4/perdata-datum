package net.avh4.data.datum.transact.commands;

import net.avh4.data.datum.primitives.Id;
import net.avh4.data.datum.store.DatumStore;
import net.avh4.data.datum.transact.Command;
import net.avh4.data.datum.transact.TransactionException;
import org.json.JSONArray;
import org.json.JSONException;

public class Add implements Command {
    private final Id entity;
    private final String action;
    private final String value;

    public Add(Id entity, String action, String value) {
        this.entity = entity;
        this.action = action;
        this.value = value;
    }

    @Override public DatumStore execute(DatumStore store) throws TransactionException {
        final String entityId = entity.id();
        final String oldValue = store.get(entityId, action);
        try {
            JSONArray array = oldValue == null ? new JSONArray() : new JSONArray(oldValue);
            array.put(value);
            store = store.set(entityId, action, array.toString());
            store = store.addIndex(action, value, entityId);
        } catch (JSONException e) {
            throw new RuntimeException("Current value is not a JSON array: " + oldValue);
        }
        return store;
    }

    @Override public DatumStore resolveTempIds(DatumStore store) {
        store = entity.resolve(store);
        return store;
    }

    @Override public String toString() {
        return "Add(" + entity + ", " + action + ", " + value + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Add add = (Add) o;

        if (action != null ? !action.equals(add.action) : add.action != null) return false;
        if (entity != null ? !entity.equals(add.entity) : add.entity != null) return false;
        if (value != null ? !value.equals(add.value) : add.value != null) return false;

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
