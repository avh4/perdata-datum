package net.avh4.data.datum.peer.java;

import net.avh4.data.datum.primitives.Id;
import net.avh4.data.datum.store.DatumStore;

public class DatabaseImpl implements DirectAccess {
    @Override public <T> T get(DatumStore store, Class<T> documentClass, Id entityId) {
        return DocumentInvocationHandler.getDocument(store, documentClass, entityId);
    }
}
