package net.avh4.data.datum.transact.commands;

import net.avh4.data.datum.primitives.Id;
import net.avh4.data.datum.store.DatumStore;
import net.avh4.data.datum.transact.Command;
import net.avh4.data.datum.transact.TransactionException;
import org.json.JSONArray;
import org.json.JSONException;

public class Remove implements Command {
    private final Id entity;
    private final String action;
    private final String value;

    public Remove(Id entity, String action, String value) {
        this.entity = entity;
        this.action = action;
        this.value = value;
    }

    @Override public DatumStore execute(DatumStore store) throws TransactionException {
        final String entityId = entity.id();
        final String oldValue = store.get(entityId, action);
        if (oldValue == null) throw new TransactionException("Array element does not exist: " + value);
        try {
            JSONArray array = new JSONArray(oldValue);
            JSONArray newArray = new JSONArray();
            int found = 0;
            for (int i = 0; i < array.length(); i++) {
                if (array.getString(i).equals(value)) {
                    found++;
                    if (found <= 1) continue;
                }
                newArray.put(array.getString(i));
            }
            if (found == 0) throw new TransactionException("Array element does not exist: " + value);
            if (newArray.length() == 0) store = store.set(entityId, action, null);
            else store = store.set(entityId, action, newArray.toString());
            if (found <= 1) store = store.removeIndex(action, value, entityId);
        } catch (JSONException e) {
            throw new RuntimeException("Not implemented");  // TODO
        }
        return store;
    }

    @Override public DatumStore resolveTempIds(DatumStore store) {
        store = entity.resolve(store);
        return store;
    }

    @Override public String toString() {
        return "Remove(" + entity + ", " + action + ", " + value + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Remove remove = (Remove) o;

        if (action != null ? !action.equals(remove.action) : remove.action != null) return false;
        if (entity != null ? !entity.equals(remove.entity) : remove.entity != null) return false;
        if (value != null ? !value.equals(remove.value) : remove.value != null) return false;

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
