package net.avh4.data.datum.transact.commands;

import net.avh4.data.datum.primitives.Id;
import net.avh4.data.datum.store.DatumStore;
import net.avh4.data.datum.transact.Command;
import net.avh4.data.datum.transact.TransactionException;
import org.json.JSONArray;
import org.json.JSONException;

public class AddRef implements Command {
    private final Id entity;
    private final String action;
    private final Id ref;

    public AddRef(Id entity, String action, Id ref) {
        this.entity = entity;
        this.action = action;
        this.ref = ref;
    }

    @Override public DatumStore execute(DatumStore store) throws TransactionException {
        final String entityId = entity.id();
        final String value = ref.id();
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
        store = ref.resolve(store);
        return store;
    }

    @Override public String toString() {
        return "AddRef(" + entity + ", " + action + ", " + ref + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddRef addRef = (AddRef) o;

        if (action != null ? !action.equals(addRef.action) : addRef.action != null) return false;
        if (entity != null ? !entity.equals(addRef.entity) : addRef.entity != null) return false;
        if (ref != null ? !ref.equals(addRef.ref) : addRef.ref != null) return false;

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
