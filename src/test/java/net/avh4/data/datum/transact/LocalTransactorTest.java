package net.avh4.data.datum.transact;

import net.avh4.data.datum.store.DatumStore;
import net.avh4.data.datum.store.service.Ref;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

public class LocalTransactorTest extends TransactorContract {

    @Mock private DatumStore committedStore;
    @Mock private Ref<DatumStore> storage;

    protected Transactor createSubject(DatumStore store) {
        stub(storage.get()).toReturn(store);
        return new LocalTransactor(storage);
    }

    @Override protected DatumStore expectedResultFor(DatumStore store_afterTransaction) {
        if (store_afterTransaction == store_afterC1) return committedStore;
        throw new RuntimeException("Unexpected transaction result: " + store_afterTransaction);
    }

    @Before @Override
    public void setUp() throws Exception {
        super.setUp();
        stub(storage.commit(store_afterC1)).toReturn(committedStore);
    }

    @Test
    public void onSuccessfulTransaction_shouldCommitToStorage() throws Exception {
        subject.transact(txn);
        verify(storage).commit(store_afterC1);
    }
}
