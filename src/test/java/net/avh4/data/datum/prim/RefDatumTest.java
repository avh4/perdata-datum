package net.avh4.data.datum.prim;

import net.avh4.data.datum.prim.Id;
import net.avh4.data.datum.prim.RefDatum;
import net.avh4.data.datum.store.DatumStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

public class RefDatumTest {

    private RefDatum subject;
    @Mock private Id entityId;
    @Mock private Id refId;
    @Mock private DatumStore store;
    @Mock private DatumStore modifiedStore1;
    @Mock private DatumStore modifiedStore2;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        stub(entityId.resolve(store)).toReturn(modifiedStore1);
        stub(refId.resolve(store)).toReturn(modifiedStore1);
        stub(entityId.resolve(modifiedStore1)).toReturn(modifiedStore2);
        stub(refId.resolve(modifiedStore1)).toReturn(modifiedStore2);
        subject = new RefDatum(entityId, "action", refId);
    }

    @Test
    public void resolveTempIds_shouldResolveEntityId() throws Exception {
        subject.resolveTempIds(store);
        verify(entityId).resolve(store);
    }

    @Test
    public void resolveTempIds_shouldResolveRefId() throws Exception {
        subject.resolveTempIds(store);
        verify(refId).resolve(modifiedStore1);
    }

    @Test
    public void resolveTempIds_shouldReturnTheModifiedStore() throws Exception {
        assertThat(subject.resolveTempIds(store)).isSameAs(modifiedStore2);
    }
}
