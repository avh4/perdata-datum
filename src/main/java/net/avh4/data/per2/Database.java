package net.avh4.data.per2;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Database {

    private final DatumStore store;
    private int nextId = 0;
    @Deprecated
    private final ArrayList<String> ids = new ArrayList<String>();

    public Database(DatumStore store) {
        this.store = store;
    }

    public String create() {
        final String entityId = "" + (++nextId);
        ids.add(entityId);
        return entityId;
    }

    public void set(String entityId, String action, Object value) {
        store.write(entityId, action, value.toString());
    }

    public void add(String entityId, String action, Object value) {
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
    }

    public void transaction(Runnable runnable) {
        runnable.run();
    }

    public <T> T[] query(Class<T> documentClass) {
        //noinspection unchecked
        final T[] results = (T[]) Array.newInstance(documentClass, 1);
        results[0] = getDocument(documentClass, ids.get(0));
        return results;
    }

    private <T> T getDocument(Class<T> documentClass, final String entityId) {
        return DocumentInvocationHandler.getDocument(store, documentClass, entityId);
    }
}
