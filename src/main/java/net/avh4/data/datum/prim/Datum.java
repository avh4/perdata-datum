package net.avh4.data.datum.prim;

import net.avh4.data.datum.store.DatumStore;

public interface Datum {
    String entityId();

    String action();

    String value();

    DatumStore resolveTempIds(DatumStore store);
}
