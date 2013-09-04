package net.avh4.data.datum.transact.commands;

import net.avh4.data.datum.prim.Id;
import net.avh4.data.datum.prim.KnownId;
import net.avh4.data.datum.store.DatumStore;
import net.avh4.data.datum.store.MemoryDatumStore;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class AddRefTest {
    private static final Id gregory = new KnownId("gregory");
    private static final Id house = new KnownId("House M.D.");
    private AddRef subject;
    private DatumStore store;

    @Before
    public void setUp() throws Exception {
        store = new MemoryDatumStore();
        subject = new AddRef(gregory, "watches", house);
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
