package net.avh4.data.datum.transact;

import net.avh4.data.datum.store.DatumStore;

public interface Command {
    DatumStore execute(DatumStore store) throws TransactionException;
}
