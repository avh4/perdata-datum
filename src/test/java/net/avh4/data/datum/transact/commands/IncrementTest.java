package net.avh4.data.datum.transact.commands;

import net.avh4.data.datum.store.DatumStore;
import net.avh4.data.datum.store.MemoryDatumStore;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class IncrementTest {
    private Increment subject;
    private DatumStore store;

    @Before
    public void setUp() throws Exception {
        store = new MemoryDatumStore();
        subject = new Increment("buster", "hey_brother");
    }

    @Test
    public void withNoStoredValue_shouldCreateCount() throws Exception {
        store = subject.execute(store);
        assertThat(store.get("buster", "hey_brother")).isEqualTo("1");
    }

    @Test
    public void withStoredValue_shouldIncrement() throws Exception {
        store = store.set("buster", "hey_brother", "13");
        store = subject.execute(store);
        assertThat(store.get("buster", "hey_brother")).isEqualTo("14");
    }

    @Test
    public void shouldIndexInNumericalOrder() throws Exception {
        store = subject.execute(store);
        assertThat(store.iterate("hey_brother", "0000000001", "0000000001")).containsOnly("buster");
    }

    @Test
    public void withStoredValue_shouldRemovePreviousIndex() throws Exception {
        store = store.set("buster", "hey_brother", "10");
        store = store.addIndex("hey_brother", "0000000010", "buster");
        store = subject.execute(store);
        assertThat(store.iterate("hey_brother", "0000000010", "0000000010")).isEmpty();
    }
}
