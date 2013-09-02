package net.avh4.data.datum.prim;

import net.avh4.data.datum.prim.Id;
import net.avh4.data.datum.prim.ValueDatum;
import net.avh4.data.datum.store.DatumStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

public class ValueDatumTest {

    private ValueDatum subject;
    @Mock private Id entityId;
    @Mock private DatumStore store;
    @Mock private DatumStore modifiedStore;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        stub(entityId.resolve(store)).toReturn(modifiedStore);
        subject = new ValueDatum(entityId, "action", "value");
    }

    @Test
    public void resolveTempIds_shouldResolveEntityId() throws Exception {
        subject.resolveTempIds(store);
        verify(entityId).resolve(store);
    }

    @Test
    public void resolveTempIds_shouldReturnTheModifiedStore() throws Exception {
        assertThat(subject.resolveTempIds(store)).isSameAs(modifiedStore);
    }
}
