package net.avh4.data.per2;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Proxy;
import java.util.ArrayList;

import static net.avh4.data.per2.DatumIntegrationTest.Book;

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

    public Book[] query(Class<Book> documentClass) {
        final Book[] results = new Book[1];
        results[0] = new Book() {
            @Override public String title() {
                return store.get(ids.get(0), "title");
            }

            @Override public Person author() {
                return getDocument(Person.class, ids.get(1));
            }

            @Override public Chapter[] chapters() {
                return new Chapter[]{
                        getDocument(Chapter.class, ids.get(2)),
                        getDocument(Chapter.class, ids.get(3)),
                };
            }
        };
        return results;
    }

    private <T> T getDocument(Class<T> documentClass, final String entityId) {
        return DocumentInvocationHandler.getDocument(store, documentClass, entityId);
    }
}
