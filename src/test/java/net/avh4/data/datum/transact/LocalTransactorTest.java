package net.avh4.data.datum.transact;

import net.avh4.data.datum.store.DatumStore;

public class LocalTransactorTest extends TransactorContract {

    protected Transactor createSubject(DatumStore store) {
        return new LocalTransactor(store);
    }
}
