package net.avh4.data.datum.transact.commands;

import net.avh4.data.datum.store.DatumStore;
import net.avh4.data.datum.transact.Command;
import net.avh4.data.datum.transact.IndexStore;
import net.avh4.data.datum.transact.KeyStore;
import net.avh4.data.datum.transact.TransactionException;
import org.json.JSONArray;
import org.json.JSONException;

public class Remove implements Command {
    private final String entityId;
    private final String action;
    private final String value;

    public Remove(String entityId, String action, String value) {
        this.entityId = entityId;
        this.action = action;
        this.value = value;
    }

    @Override public DatumStore execute(DatumStore store) throws TransactionException {
        String oldValue = store.get(entityId, action);
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
            else store = store.set(entityId, action,  newArray.toString());
            if (found <= 1) store = store.removeIndex(action, value, entityId);
        } catch (JSONException e) {
            throw new RuntimeException("Not implemented");  // TODO
        }
        return store;
    }
}
