package net.avh4.util.data;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.HashMap;

import static org.fest.assertions.Assertions.assertThat;

public class BTreeTest {
    private BTree t;

    @Rule public PrintFailedTree rule = new PrintFailedTree();

    @Before
    public void setUp() throws Exception {
        TestBTreeStorage storage = new TestBTreeStorage();
        t = new BTree(storage, 2);
    }

    @Test
    public void insertRoot() throws Exception {
        BTree t = this.t.insert("M", "em");
        assertNode(t, 0, "M", "em");
    }

    @Test
    public void insertLeft() throws Exception {
        BTree t = this.t.insert("M", "em").insert("A", "ay");
        assertNode(t, 0, "A", "ay");
        assertNode(t, 1, "M", "em");
    }

    @Test
    public void insertRight() throws Exception {
        BTree t = this.t.insert("M", "em").insert("Z", "zee");
        assertNode(t, 0, "M", "em");
        assertNode(t, 1, "Z", "zee");
    }

    @Test
    public void insertLeftLeft() throws Exception {
        BTree t = this.t.insert("M", "em").insert("B", "bee").insert("A", "ay");
        assertNode(t, 0, "A", "ay");
        assertNode(t, 1, "B", "bee");
        assertNode(t, 2, "M", "em");
    }

    @Test
    public void insertLeftRight() throws Exception {
        BTree t = this.t.insert("M", "em").insert("A", "ay").insert("B", "bee");
        assertNode(t, 0, "A", "ay");
        assertNode(t, 1, "B", "bee");
        assertNode(t, 2, "M", "em");
    }

    @Test
    public void insertRightRight() throws Exception {
        BTree t = this.t.insert("M", "em").insert("Y", "wye").insert("Z", "zee");
        assertNode(t, 0, "M", "em");
        assertNode(t, 1, "Y", "wye");
        assertNode(t, 2, "Z", "zee");
    }

    @Test
    public void splitOnNewKey() throws Exception {
        BTree t = this.t.insert("A", "ay").insert("B", "bee").insert("D", "dee").insert("E", "ee");
        t = t.insert("C", "cee");
        assertNode(t, 0, "D");
        assertNode(t.node(0), 0, "A", "ay");
        assertNode(t.node(0), 1, "B", "bee");
        assertNode(t.node(0), 2, "C", "cee");
        assertNode(t.node(1), 0, "D", "dee");
        assertNode(t.node(1), 1, "E", "ee");
    }

    @Test
    public void splitWithNewKeyLeft() throws Exception {
        t = t.insert("B", "bee").insert("C", "cee").insert("D", "dee").insert("E", "ee");
        t = t.insert("A", "ay");
        assertNode(t, 0, "D");
        assertNode(t.node(0), 0, "A", "ay");
        assertNode(t.node(0), 1, "B", "bee");
        assertNode(t.node(0), 2, "C", "cee");
        assertNode(t.node(1), 0, "D", "dee");
        assertNode(t.node(1), 1, "E", "ee");
    }

    @Test
    public void splitWithNewKeyRight() throws Exception {
        t = t.insert("A", "ay").insert("B", "bee").insert("C", "cee").insert("D", "dee");
        t = t.insert("E", "ee");
        assertNode(t, 0, "C");
        assertNode(t.node(0), 0, "A", "ay");
        assertNode(t.node(0), 1, "B", "bee");
        assertNode(t.node(1), 0, "C", "cee");
        assertNode(t.node(1), 1, "D", "dee");
        assertNode(t.node(1), 2, "E", "ee");
    }

    private void assertNode(BTree t, int i, String key) {
        assertThat(t.keys[i]).isEqualTo(key);
        assertThat(t.vals).isNull();
    }

    private void assertNode(BTree t, int i, String key, String val) {
        assertThat(t.keys[i]).isEqualTo(key);
        assertThat(t.vals[i]).isEqualTo(val);
        assertThat(t.nodes).isNull();
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

    private class PrintFailedTree implements TestRule {
        @Override public Statement apply(final Statement base, final Description description) {
            return new Statement() {
                @Override public void evaluate() throws Throwable {
                    try {
                        base.evaluate();
                    } catch (AssertionError e) {
                        System.out.println(description + " failed with: \n  t = " + t);
                        throw e;
                    }
                }
            };
        }
    }
}
