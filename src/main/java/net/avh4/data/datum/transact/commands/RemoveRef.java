package net.avh4.data.datum.transact.commands;

import net.avh4.data.datum.primitives.Id;
import net.avh4.data.datum.store.DatumStore;
import net.avh4.data.datum.transact.Command;
import net.avh4.data.datum.transact.TransactionException;
import org.json.JSONArray;
import org.json.JSONException;

public class RemoveRef implements Command {
    private final Id entity;
    private final String action;
    private final Id ref;

    public RemoveRef(Id entity, String action, Id ref) {
        this.entity = entity;
        this.action = action;
        this.ref = ref;
    }

    @Override public DatumStore execute(DatumStore store) throws TransactionException {
        final String entityId = entity.id();
        final String value = ref.id();
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
        store = ref.resolve(store);
        return store;
    }

    @Override public String toString() {
        return "RemoveRef(" + entity + ", " + action + ", " + ref + ')';
    }
}
