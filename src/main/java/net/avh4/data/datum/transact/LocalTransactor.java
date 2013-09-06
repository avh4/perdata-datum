package net.avh4.data.datum.transact;

import net.avh4.data.datum.store.DatumStore;
import net.avh4.data.datum.store.service.Ref;

import java.io.IOException;

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
        try {
            return storage.commit(store);
        } catch (IOException e) {
            throw new TransactionException("Could not commit transaction", e);
        }
    }
}
