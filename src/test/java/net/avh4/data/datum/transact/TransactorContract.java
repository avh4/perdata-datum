package net.avh4.data.datum.transact;

import net.avh4.data.datum.store.DatumStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

public abstract class TransactorContract {
    private Transactor subject;
    @Mock private Transaction txn;
    @Mock private Command c1;
    @Mock private Command c2;
    @Mock private DatumStore store;
    @Mock private DatumStore store2;
    @Mock private DatumStore store3;

    protected abstract Transactor createSubject(DatumStore store);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        stub(c1.resolveTempIds(store)).toReturn(store);
        stub(store.set(anyString(), anyString(), anyString())).toReturn(store);

        stub(txn.commands()).toReturn(Arrays.asList(c1));
        stub(c1.execute(store)).toReturn(store2);

        subject = createSubject(store);
    }

    @Test
    public void shouldExecuteCommands() throws Exception {
        subject.transact(txn);
        verify(c1).execute(store);
    }

    @Test
    public void shouldHaveResult() throws Exception {
        assertThat(subject.transact(txn)).isSameAs(store2);
    }

    @Test
    public void shouldResolveTempIds() throws Exception {
        subject.transact(txn);
        verify(c1).resolveTempIds(store);
    }
}
