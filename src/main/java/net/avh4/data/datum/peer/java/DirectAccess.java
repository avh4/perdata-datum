package net.avh4.data.datum.peer.java;

import net.avh4.data.datum.primitives.Id;
import net.avh4.data.datum.store.DatumStore;

public interface DirectAccess {
    <T> T get(DatumStore store, Class<T> documentClass, Id entityId);
}
