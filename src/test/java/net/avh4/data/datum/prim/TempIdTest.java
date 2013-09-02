package net.avh4.data.datum.prim;

import net.avh4.data.datum.prim.TempId;
import net.avh4.data.datum.store.DatumStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TempIdTest {
    private TempId subject;
    @Mock private DatumStore store;
    @Mock private DatumStore modifiedStore;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subject = new TempId();
        stub(store.createId(subject)).toReturn(modifiedStore);
    }

    @Test
    public void id_shouldReturnMappedId() throws Exception {
        subject.mapId("A");
        assertThat(subject.id()).isEqualTo("A");
    }

    @Test(expected = IllegalStateException.class)
    public void id_withNoMappedId_shouldThrow() throws Exception {
        subject.id();
    }

    @Test(expected = IllegalStateException.class)
    public void id_withNoAssignedTempId_shouldThrow() throws Exception {
        subject.id();
    }

    @Test(expected = IllegalStateException.class)
    public void mapId_twice_shouldThrow() throws Exception {
        subject.mapId("A");
        subject.mapId("B");
    }

    @Test
    public void resolve_shouldRequestId() throws Exception {
        subject.resolve(store);
        verify(store).createId(subject);
    }

    @Test
    public void resolve_shouldReturnModifiedStore() throws Exception {
        assertThat(subject.resolve(store)).isSameAs(modifiedStore);
    }

    @Test
    public void resolve_whenMapped_shouldDoNothing() throws Exception {
        subject.mapId("A");
        subject.resolve(store);
        verifyZeroInteractions(store);
    }

    @Test
    public void resolve_whenMapped_shouldReturnStore() throws Exception {
        subject.mapId("A");
        assertThat(subject.resolve(store)).isSameAs(store);
    }
}
