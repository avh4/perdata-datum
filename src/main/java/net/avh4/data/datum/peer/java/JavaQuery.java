package net.avh4.data.datum.peer.java;

import net.avh4.data.datum.peer.Query;
import net.avh4.data.datum.prim.KnownId;
import net.avh4.data.datum.store.DatumStore;

import java.lang.reflect.Array;

public class JavaQuery<T> {
    private final Class<T> resultClass;
    private final Query query;

    public JavaQuery(Class<T> resultClass, String index, String value) {
        this(resultClass, new Query(index, value));
    }

    public JavaQuery(Class<T> resultClass, Query query) {
        this.resultClass = resultClass;
        this.query = query;
    }

    public T[] execute(DatumStore store) {
        final KnownId[] ids = query.execute(store);
        //noinspection unchecked
        T[] results = (T[]) Array.newInstance(resultClass, ids.length);
        for (int i = 0; i < ids.length; i++) {
            results[i] = DocumentInvocationHandler.getDocument(store, resultClass, ids[i]);
        }
        return results;
    }
}
