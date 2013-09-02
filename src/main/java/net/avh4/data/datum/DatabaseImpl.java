package net.avh4.data.datum;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DatabaseImpl implements Database, TransactionContext {

    private final DatumStore store;
    private int nextId = 0;
    @Deprecated
    private final ArrayList<String> ids = new ArrayList<String>();

    public DatabaseImpl(DatumStore store) {
        this.store = store;
    }

    @Override public String create() {
        final String entityId = "" + (++nextId);
        ids.add(entityId);
        return entityId;
    }

    @Override public Void set(String entityId, String action, Object value) {
        store.write(entityId, action, value.toString());
        return null;
    }

    @Override public Void add(String entityId, String action, Object value) {
        final String currentValue = store.get(entityId, action);
        try {
            JSONArray array;
            if (currentValue == null) {
                array = new JSONArray();
            } else {
                array = new JSONArray(currentValue);
            }
            array.put(value);
            store.write(entityId, action, array.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override public <T> T transact(Transaction<T> transaction) {
        return transaction.run(this);
    }

    @Override public <T> T[] query(Class<T> documentClass) {
        //noinspection unchecked
        final T[] results = (T[]) Array.newInstance(documentClass, 1);
        results[0] = get(documentClass, ids.get(0));
        return results;
    }

    @Override public <T> T get(Class<T> documentClass, String entityId) {
        return DocumentInvocationHandler.getDocument(store, documentClass, entityId);
    }
}
