package net.avh4.data.datum.primitives;

import net.avh4.data.datum.store.DatumStore;

public interface Id {
    String id();

    DatumStore resolve(DatumStore store);
}
