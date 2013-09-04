package net.avh4.util.data;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.IdentityHashMap;

import static org.fest.assertions.Assertions.assertThat;

public class BTreeTest {
    private BTree subject;
    private TestBTreeStorage storage;

    @Before
    public void setUp() throws Exception {
        storage = new TestBTreeStorage();
        subject = new BTree(storage, 2);
    }

    @Test
    public void insertRoot() throws Exception {
        BTree t = subject.insert("M", "em");
        assertNode(t, 0, "M", "em");
    }

    @Test
    public void insertLeft() throws Exception {
        BTree t = subject.insert("M", "em").insert("A", "ay");
        assertNode(t, 0, "A", "ay");
        assertNode(t, 1, "M", "em");
    }

    @Test
    public void insertRight() throws Exception {
        BTree t = subject.insert("M", "em").insert("Z", "zee");
        assertNode(t, 0, "M", "em");
        assertNode(t, 1, "Z", "zee");
    }

    @Test
    public void insertLeftLeft() throws Exception {
        BTree t = subject.insert("M", "em").insert("B", "bee").insert("A", "ay");
        assertNode(t, 0, "A", "ay");
        assertNode(t, 1, "B", "bee");
        assertNode(t, 2, "M", "em");
    }

    private void assertNode(BTree t, int i, String key, String val) {
        assertThat(t.keys[i]).isEqualTo(key);
        assertThat(t.vals[i]).isEqualTo(val);
    }

    private static class TestBTreeStorage implements BTreeStorage {
        private HashMap<Long, BTree> map = new HashMap<Long, BTree>();
        private long nextId = 100;

        @Override public long write(BTree content) {
            if (map.containsKey(nextId)) throw new RuntimeException("Page " + nextId + " was already written");
            map.put(nextId, content);
            return nextId++;
        }

        @Override public BTree get(long id) {
            if (!map.containsKey(id)) throw new RuntimeException("Requested page " + id + " was never written");
            return map.get(id);
        }
    }
}
