package net.avh4.data.datum.transact;

import net.avh4.data.datum.store.DatumStore;

public class LocalTransactor implements Transactor {

    private final Ref<DatumStore> storage;

    public LocalTransactor(Ref<DatumStore> storage) {
        this.storage = storage;
    }

    @Override public DatumStore transact(Transaction transaction) throws TransactionException {
        DatumStore store = storage.get();
        for (Command command : transaction.commands()) {
            store = command.resolveTempIds(store);
            store = command.execute(store);
        }
        return storage.commit(store);
    }
}
