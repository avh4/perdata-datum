package net.avh4.data.datum.transact;

import net.avh4.data.datum.store.DatumStore;

public class LocalTransactor implements Transactor {
    private DatumStore store;

    public LocalTransactor(DatumStore store) {
        this.store = store;
    }

    @Override public DatumStore transact(Transaction transaction) throws TransactionException {
        DatumStore store = this.store;
        for (Command command : transaction.commands()) {
            store = command.resolveTempIds(store);
            store = command.execute(store);
        }
        return (this.store = store);
    }
}
