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
        t = t.insert("M", "em");
        assertTree(t, "[M(em)...]");
    }

    @Test
    public void insertLeft() throws Exception {
        t = t.insert("M", "em").insert("A", "ay");
        assertTree(t, "[A(ay)M(em)..]");
    }

    @Test
    public void insertRight() throws Exception {
        t = t.insert("M", "em").insert("Z", "zee");
        assertTree(t, "[M(em)Z(zee)..]");
    }

    @Test
    public void insertLeftLeft() throws Exception {
        t = t.insert("M", "em").insert("B", "bee").insert("A", "ay");
        assertTree(t, "[A(ay)B(bee)M(em).]");
    }

    @Test
    public void insertLeftRight() throws Exception {
        t = t.insert("M", "em").insert("A", "ay").insert("B", "bee");
        assertTree(t, "[A(ay)B(bee)M(em).]");
    }

    @Test
    public void insertRightRight() throws Exception {
        t = t.insert("M", "em").insert("Y", "wye").insert("Z", "zee");
        assertTree(t, "[M(em)Y(wye)Z(zee).]");
    }

    @Test
    public void splitOnNewKey() throws Exception {
        t = t.insert("A", "ay").insert("B", "bee").insert("D", "dee").insert("E", "ee");
        t = t.insert("C", "cee");
        assertTree(t, "[[A(ay)B(bee)C(cee).]D[D(dee)E(ee)..]...]");
    }

    @Test
    public void splitWithNewKeyLeft() throws Exception {
        t = t.insert("B", "bee").insert("C", "cee").insert("D", "dee").insert("E", "ee");
        t = t.insert("A", "ay");
        assertTree(t, "[[A(ay)B(bee)C(cee).]D[D(dee)E(ee)..]...]");
    }

    @Test
    public void splitWithNewKeyRight() throws Exception {
        t = t.insert("A", "ay").insert("B", "bee").insert("C", "cee").insert("D", "dee");
        t = t.insert("E", "ee");
        assertTree(t, "[[A(ay)B(bee)..]C[C(cee)D(dee)E(ee).]...]");
    }

    @Test
    public void splitWithNewKeyEqual() throws Exception {
        t = t.insert("A", "ay").insert("B", "bee").insert("C", "cee").insert("D", "dee");
        t = t.insert("C", "cee2");
        assertTree(t, "[[A(ay)B(bee)..]C[C(cee)C(cee2)D(dee).]...]");
    }

    @Test
    public void insertLeftChild() throws Exception {
        t = t.insert("B", "bee").insert("C", "cee").insert("D", "dee").insert("E", "ee").insert("F", "ef");
        t = t.insert("A", "ay");
        assertTree(t, "[[A(ay)B(bee)C(cee).]D[D(dee)E(ee)F(ef).]...]");
    }

    @Test
    public void insertRightChild() throws Exception {
        t = t.insert("A", "ay").insert("B", "bee").insert("C", "cee").insert("D", "dee").insert("E", "ee");
        t = t.insert("F", "ef");
        assertTree(t, "[[A(ay)B(bee)..]C[C(cee)D(dee)E(ee)F(ef)]...]");
    }

    @Test
    public void splitLeftChild() throws Exception {
        t = t.insert("B", "bee").insert("C", "cee").insert("F", "ef").insert("G", "gee");
        t = t.insert("E", "ee").insert("D", "dee");
        assertTree(t, "[[B(bee)C(cee)D(dee)E(ee)]F[F(ef)G(gee)..]...]");
        t = t.insert("A", "ay");
        assertTree(t, "[[A(ay)B(bee)C(cee).]D[D(dee)E(ee)..]F[F(ef)G(gee)..]..]");
    }

    @Test
    public void splitRightChild() throws Exception {
        t = t.insert("A", "ay").insert("B", "bee").insert("C", "cee").insert("D", "dee");
        t = t.insert("E", "ee").insert("F", "ef");
        assertTree(t, "[[A(ay)B(bee)..]C[C(cee)D(dee)E(ee)F(ef)]...]");
        t = t.insert("G", "gee");
        assertTree(t, "[[A(ay)B(bee)..]C[C(cee)D(dee)..]E[E(ee)F(ef)G(gee).]..]");
    }

    @Test
    public void insertToThirdChild() throws Exception {
        t = t.insert("A", "ay").insert("B", "bee").insert("C", "cee").insert("D", "dee");
        t = t.insert("E", "ee").insert("F", "ef").insert("G", "gee");
        assertTree(t, "[[A(ay)B(bee)..]C[C(cee)D(dee)..]E[E(ee)F(ef)G(gee).]..]");
        t = t.insert("H", "aitch");
        assertTree(t, "[[A(ay)B(bee)..]C[C(cee)D(dee)..]E[E(ee)F(ef)G(gee)H(aitch)]..]");
    }

    @Test
    public void splitThirdChild() throws Exception {
        t = t.insert("A", "ay").insert("B", "bee").insert("C", "cee").insert("D", "dee");
        t = t.insert("E", "ee").insert("F", "ef").insert("G", "gee").insert("H", "aitch");
        assertTree(t, "[[A(ay)B(bee)..]C[C(cee)D(dee)..]E[E(ee)F(ef)G(gee)H(aitch)]..]");
        t = t.insert("I", "ai");
        assertTree(t, "[[A(ay)B(bee)..]C[C(cee)D(dee)..]E[E(ee)F(ef)..]G[G(gee)H(aitch)I(ai).].]");
    }

    @Test
    public void splitFourthChild() throws Exception {
        t = t.insert("A", "ay").insert("B", "bee").insert("C", "cee").insert("D", "dee");
        t = t.insert("E", "ee").insert("F", "ef").insert("G", "gee").insert("H", "aitch");
        t = t.insert("I", "ai").insert("J", "jay").insert("K", "kay");
        assertTree(t, "[[A(ay)B(bee)..]C[C(cee)D(dee)..]E[E(ee)F(ef)..]G[G(gee)H(aitch)..]I[I(ai)J(jay)K(kay).]]");
    }

    @Test
    public void insertToFifthChild() throws Exception {
        t = t.insert("A", "ay").insert("B", "bee").insert("C", "cee").insert("D", "dee");
        t = t.insert("E", "ee").insert("F", "ef").insert("G", "gee").insert("H", "aitch");
        t = t.insert("I", "ai").insert("J", "jay").insert("K", "kay").insert("L", "el");
        assertTree(t, "[[A(ay)B(bee)..]C[C(cee)D(dee)..]E[E(ee)F(ef)..]G[G(gee)H(aitch)..]I[I(ai)J(jay)K(kay)L(el)]]");
    }

    @Test
    public void splitLeftChildOfThree() throws Exception {
        t = t.insert("K", "kay").insert("L", "el");
        t = t.insert("I", "ai").insert("J", "jay");
        t = t.insert("G", "gee").insert("H", "aitch");
        t = t.insert("E", "ee").insert("F", "ef");
        assertTree(t, "[[E(ee)F(ef)G(gee)H(aitch)]I[I(ai)J(jay)..]K[K(kay)L(el)..]..]");
        t = t.insert("D", "dee");
        assertTree(t, "[[D(dee)E(ee)F(ef).]G[G(gee)H(aitch)..]I[I(ai)J(jay)..]K[K(kay)L(el)..].]");
    }

    @Test
    public void splitSecondLevelLeft() throws Exception {
        t = t.insert("K", "kay").insert("L", "el");
        t = t.insert("I", "ai").insert("J", "jay");
        t = t.insert("G", "gee").insert("H", "aitch");
        t = t.insert("E", "ee").insert("F", "ef");
        t = t.insert("C", "cee").insert("D", "dee");
        t = t.insert("A", "ay").insert("B", "bee");
        assertTree(t, "[[A(ay)B(bee)C(cee)D(dee)]E[E(ee)F(ef)..]G[G(gee)H(aitch)..]I[I(ai)J(jay)..]K[K(kay)L(el)..]]");
        t = t.insert("A", "ay2");
        assertTree(t, "[[[A(ay)A(ay2)B(bee).]C[C(cee)D(dee)..]E[E(ee)F(ef)..]..]G[[G(gee)H(aitch)..]I[I(ai)J(jay)..]K[K(kay)L(el)..]..]...]");
    }

    @Test
    public void splitSecondLevelRight() throws Exception {
        t = t.insert("A", "ay").insert("B", "bee").insert("C", "cee").insert("D", "dee");
        t = t.insert("E", "ee").insert("F", "ef").insert("G", "gee").insert("H", "aitch");
        t = t.insert("I", "ai").insert("J", "jay").insert("K", "kay").insert("L", "el");
        assertTree(t, "[[A(ay)B(bee)..]C[C(cee)D(dee)..]E[E(ee)F(ef)..]G[G(gee)H(aitch)..]I[I(ai)J(jay)K(kay)L(el)]]");
        t = t.insert("M", "em");
        assertTree(t, "[[[A(ay)B(bee)..]C[C(cee)D(dee)..]E[E(ee)F(ef)..]..]G[[G(gee)H(aitch)..]I[I(ai)J(jay)..]K[K(kay)L(el)M(em).]..]...]");
    }

    private void assertTree(BTree t, String description) {
        assertThat(description(t)).isEqualTo(description);
    }

    private String description(BTree t) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i <= t.keys.length; i++) {
            if (t.nodes != null && t.nodes[i] != 0) sb.append(description(t.node(i)));
            if (i == t.keys.length) break;
            if (t.keys[i] == null) sb.append(".");
            else {
                sb.append(t.keys[i]);
                if (t.vals != null && t.vals[i] != null) sb.append("(").append(t.vals[i]).append(")");
            }
        }
        sb.append("]");
        return sb.toString();
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
