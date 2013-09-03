package net.avh4.data.datum.transact;

import net.avh4.data.datum.store.DatumStore;

public interface Transactor {
    DatumStore transact(Transaction transaction) throws TransactionException;
}
