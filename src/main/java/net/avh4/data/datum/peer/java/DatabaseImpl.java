package net.avh4.data.datum.peer.java;

import net.avh4.data.datum.prim.Id;
import net.avh4.data.datum.store.DatumStore;

public class DatabaseImpl implements Query, DirectAccess {

    @Override public <T> T[] query(DatumStore store, Class<T> documentClass) {
//        noinspection unchecked
//        final T[] results = (T[]) Array.newInstance(documentClass, 1);
//        results[0] = get(documentClass, ids.get(0));
//        return results;
        throw new RuntimeException("Not implemented");  // TODO
    }

    @Override public <T> T get(DatumStore store, Class<T> documentClass, Id entityId) {
        return DocumentInvocationHandler.getDocument(store, documentClass, entityId);
    }
}
