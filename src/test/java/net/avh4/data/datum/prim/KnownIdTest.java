package net.avh4.data.datum.prim;

import net.avh4.data.datum.prim.KnownId;
import net.avh4.data.datum.store.DatumStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verifyZeroInteractions;

public class KnownIdTest {
    @Mock private DatumStore store;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void withSameId_isEqual() throws Exception {
        assertThat(new KnownId("a")).isEqualTo(new KnownId("a"));
    }
    @Test
    public void withDifferentId_isNotEqual() throws Exception {
        assertThat(new KnownId("a")).isNotEqualTo(new KnownId("b"));
    }

    @Test
    public void hasId() throws Exception {
        assertThat(new KnownId("a").id()).isEqualTo("a");
    }

    @Test
    public void resolve_shouldReturnStore() throws Exception {
        assertThat(new KnownId("a").resolve(store)).isSameAs(store);
        verifyZeroInteractions(store);
    }
}
