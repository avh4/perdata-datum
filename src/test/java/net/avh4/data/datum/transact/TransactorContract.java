package net.avh4.data.datum.transact;

import fj.data.List;
import net.avh4.data.datum.prim.Datum;
import net.avh4.data.datum.prim.Id;
import net.avh4.data.datum.prim.KnownId;
import net.avh4.data.datum.prim.TempId;
import net.avh4.data.datum.store.DatumStore;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

public abstract class TransactorContract {
    private static final Id robot = new KnownId("robot");
    private Transactor subject;
    @Mock private Transaction txn;
    @Mock private DatumStore store;
    private TempId temp1;
    private TempId temp2;
    @Mock private Datum robot_loves_kitty;

    protected abstract Transactor createSubject(DatumStore store);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        temp1 = new TempId();
        temp2 = new TempId();

        stub(robot_loves_kitty.resolveTempIds(store)).toReturn(store);

        stub(txn.assertions()).toReturn(List.<Datum>nil());
        stub(txn.additions()).toReturn(List.<Datum>nil());

        subject = createSubject(store);
    }

    @Test
    public void shouldWriteNewAssertions() throws Exception {
        stub(txn.assertions()).toReturn(Arrays.asList(robot_loves_kitty));
        subject.transact(txn);
        verify(store).write(robot_loves_kitty);
    }

    @Test
    public void shouldWriteNewAdditions() throws Exception {
        stub(txn.additions()).toReturn(Arrays.asList(robot_loves_kitty));
        subject.transact(txn);
        verify(store).add(robot_loves_kitty);
    }

    @Test
    public void shouldHaveResult() throws Exception {
        Assertions.assertThat(subject.transact(txn)).isNotNull();
    }

    @Test
    public void shouldResolveTempIdsForAssertedDatums() throws Exception {
        stub(txn.assertions()).toReturn(Arrays.asList(robot_loves_kitty));
        subject.transact(txn);
        verify(robot_loves_kitty).resolveTempIds(store);
    }

    @Test
    public void shouldResolveTempIdsForAddedDatums() throws Exception {
        stub(txn.additions()).toReturn(Arrays.asList(robot_loves_kitty));
        subject.transact(txn);
        verify(robot_loves_kitty).resolveTempIds(store);
    }
}
