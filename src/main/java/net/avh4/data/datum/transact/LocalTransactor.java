package net.avh4.data.datum.transact;

import net.avh4.data.datum.prim.Datum;
import net.avh4.data.datum.store.DatumStore;
import net.avh4.data.datum.transact.commands.Add;
import net.avh4.data.datum.transact.commands.Set;

public class LocalTransactor implements Transactor {
    private DatumStore store;

    public LocalTransactor(DatumStore store) {
        this.store = store;
    }

    @Override public DatumStore transact(Transaction transaction) throws TransactionException {
        DatumStore store = this.store;
        for (Datum datum : transaction.assertions()) {
            store = datum.resolveTempIds(store);
            final Command command = new Set(datum.entityId(), datum.action(), datum.value());
            store = command.execute(store);
        }
        for (Datum datum : transaction.additions()) {
            store = datum.resolveTempIds(store);
            Command command = new Add(datum.entityId(), datum.action(), datum.value());
            store = command.execute(store);
        }
        return (this.store = store);
    }
}
