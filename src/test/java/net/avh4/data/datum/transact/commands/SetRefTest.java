package net.avh4.data.datum.transact.commands;

import net.avh4.data.datum.prim.Id;
import net.avh4.data.datum.prim.KnownId;
import net.avh4.data.datum.store.DatumStore;
import net.avh4.data.datum.store.MemoryDatumStore;
import net.avh4.test.junit.Nested;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(Nested.class)
public class SetRefTest {
    private static final Id robot = new KnownId("robot");
    private static final Id kitty = new KnownId("kitty");
    private SetRef subject;
    private DatumStore store;

    @Before
    public void setUp() throws Exception {
        store = new MemoryDatumStore();
        subject = new SetRef(robot, "loves", kitty);
    }

    @Test
    public void shouldStoreValue() throws Exception {
        store = subject.execute(store);
        assertThat(store.get("robot", "loves")).isEqualTo("kitty");
    }

    @Test
    public void shouldAddIndex() throws Exception {
        store = subject.execute(store);
        assertThat(store.iterate("loves", "kitty", "kitty")).containsOnly("robot");
    }

    @Test
    public void withPreviousValue_shouldRemoveOldIndex() throws Exception {
        store = store.set("robot", "loves", "doggy");
        store = subject.execute(store);
        assertThat(store.iterate("loves", "doggy", "doggy")).isEmpty();
    }
}
