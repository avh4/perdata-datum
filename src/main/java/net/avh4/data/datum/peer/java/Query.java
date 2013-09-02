package net.avh4.data.datum.peer.java;

import net.avh4.data.datum.store.DatumStore;

public interface Query {
    <T> T[] query(DatumStore store, Class<T> documentClass);
}
