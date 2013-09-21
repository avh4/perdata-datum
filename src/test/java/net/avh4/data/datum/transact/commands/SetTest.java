package net.avh4.data.datum.transact.commands;

import net.avh4.data.datum.primitives.Id;
import net.avh4.data.datum.primitives.KnownId;
import net.avh4.data.datum.store.DatumStore;
import net.avh4.data.datum.store.MemoryDatumStore;
import net.avh4.test.junit.Nested;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(Nested.class)
public class SetTest {
    private static final Id robot = new KnownId("robot");
    private Set subject;
    private DatumStore store;

    @Before
    public void setUp() throws Exception {
        store = new MemoryDatumStore();
        subject = new Set(robot, "loves", "kitty");
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
    public void withNull_shouldNotAddIndex() throws Exception {
        store = new Set(robot, "loves", null).execute(store);
        assertThat(store.iterate("loves", null, null)).isEmpty();
    }

    @Test
    public void withPreviousValue_shouldRemoveOldIndex() throws Exception {
        store = store.set("robot", "loves", "doggy");
        store = subject.execute(store);
        assertThat(store.iterate("loves", "doggy", "doggy")).isEmpty();
    }
}
