package net.avh4.data.datum.transact;

import net.avh4.data.datum.prim.Datum;
import net.avh4.data.datum.store.DatumStore;

public class LocalTransactor implements Transactor {
    private DatumStore store;

    public LocalTransactor(DatumStore store) {
        this.store = store;
    }

    @Override public DatumStore transact(Transaction transaction) {
        DatumStore store = this.store;
        for (Datum datum : transaction.assertions()) {
            store = datum.resolveTempIds(store);
            store = store.set(datum);
        }
        for (Datum datum : transaction.additions()) {
            store = datum.resolveTempIds(store);
            store = store.add(datum);
        }
        return (this.store = store);
    }
}
