package net.avh4.data.datum.transact.commands;

import net.avh4.data.datum.store.DatumStore;
import net.avh4.data.datum.store.MemoryDatumStore;
import net.avh4.data.datum.transact.TransactionException;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class RemoveTest {
    private Remove subject;
    private DatumStore store;

    @Before
    public void setUp() throws Exception {
        store = new MemoryDatumStore();
        subject = new Remove("gregory", "watches", "House M.D.");
    }

    @Test
    public void shouldRemoveFromJsonArray() throws Exception {
        store = store.set("gregory", "watches", "[\"Scrubs\",\"House M.D.\"]");
        store = subject.execute(store);
        assertThat(store.get("gregory", "watches")).isEqualTo("[\"Scrubs\"]");
    }

    @Test
    public void removingLastValue_shouldRemoveJsonArray() throws Exception {
        store = store.set("gregory", "watches", "[\"House M.D.\"]");
        store = subject.execute(store);
        assertThat(store.get("gregory", "watches")).isNull();
    }

    @Test(expected = TransactionException.class)
    public void whenValueIsNotPresent_shouldThrow() throws Exception {
        store = store.set("gregory", "watches", "[\"Here Comes Honey Boo Boo\"]");
        subject.execute(store);
    }

    @Test(expected = TransactionException.class)
    public void whenValueIsNotSet_shouldThrow() throws Exception {
        subject.execute(store);
    }

    @Test
    public void shouldRemoveFromIndex() throws Exception {
        store = store.set("gregory", "watches", "[\"Scrubs\",\"House M.D.\"]");
        store = store.addIndex("watches", "House M.D.", "gregory");
        store = subject.execute(store);
        assertThat(store.iterate("watches", "House M.D.", "House M.D.")).isEmpty();
    }

    @Test
    public void withMultipleMatches_shouldOnlyRemoveOneValue() throws Exception {
        store = store.set("gregory", "watches", "[\"Scrubs\",\"House M.D.\",\"House M.D.\"]");
        store = subject.execute(store);
        assertThat(store.get("gregory", "watches")).isEqualTo("[\"Scrubs\",\"House M.D.\"]");
    }

    @Test
    public void withMultipleMatches_shouldNotRemoveFromIndex() throws Exception {
        store = store.set("gregory", "watches", "[\"Scrubs\",\"House M.D.\",\"House M.D.\"]");
        store = store.addIndex("watches", "House M.D.", "gregory");
        store = subject.execute(store);
        assertThat(store.iterate("watches", "House M.D.", "House M.D.")).containsOnly("gregory");
    }
}
