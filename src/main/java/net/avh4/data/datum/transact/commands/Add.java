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
}
