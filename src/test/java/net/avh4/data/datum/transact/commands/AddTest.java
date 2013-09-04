package net.avh4.data.datum.transact.commands;

import net.avh4.data.datum.primitives.Id;
import net.avh4.data.datum.primitives.KnownId;
import net.avh4.data.datum.store.DatumStore;
import net.avh4.data.datum.store.MemoryDatumStore;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class AddTest {
    private Add subject;
    private DatumStore store;
    private static final Id gregory = new KnownId("gregory");

    @Before
    public void setUp() throws Exception {
        store = new MemoryDatumStore();
        subject = new Add(gregory, "watches", "House M.D.");
    }

    @Test
    public void withNoStoredValue_shouldCreateJsonArray() throws Exception {
        store = subject.execute(store);
        assertThat(store.get("gregory", "watches")).isEqualTo("[\"House M.D.\"]");
    }

    @Test
    public void withStoredValue_shouldAddToJsonArray() throws Exception {
        store = store.set("gregory", "watches", "[\"Scrubs\"]");
        store = subject.execute(store);
        assertThat(store.get("gregory", "watches")).isEqualTo("[\"Scrubs\",\"House M.D.\"]");
    }

    @Test
    public void shouldAddToIndex() throws Exception {
        store = subject.execute(store);
        assertThat(store.iterate("watches", "House M.D.", "House M.D.")).containsOnly("gregory");
    }
}
