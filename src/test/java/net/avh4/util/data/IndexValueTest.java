package net.avh4.util.data;

import fj.Ord;
import fj.Ordering;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class IndexValueTest {
    private IndexValue<String, Integer> aA = new IndexValue<String, Integer>("a", null, false);
    private IndexValue<String, Integer> a1 = new IndexValue<String, Integer>("a", 1);
    private IndexValue<String, Integer> a2 = new IndexValue<String, Integer>("a", 2);
    private IndexValue<String, Integer> aZ = new IndexValue<String, Integer>("a", null, true);
    private IndexValue<String, Integer> b1 = new IndexValue<String, Integer>("b", 1);

    @Test
    public void ord_shouldOrderDifferingKeys() throws Exception {
        assertThat(IndexValue.<String, Integer>ord().compare(a1, b1)).isEqualTo(Ordering.LT);
        assertThat(IndexValue.<String, Integer>ord().compare(a1, a1)).isEqualTo(Ordering.EQ);
        assertThat(IndexValue.<String, Integer>ord().compare(b1, a1)).isEqualTo(Ordering.GT);
    }

    @Test
    public void ord_shouldOrderDifferingValues() throws Exception {
        assertThat(IndexValue.<String, Integer>ord().compare(a1, a2)).isEqualTo(Ordering.LT);
        assertThat(IndexValue.<String, Integer>ord().compare(a1, a1)).isEqualTo(Ordering.EQ);
        assertThat(IndexValue.<String, Integer>ord().compare(a2, a1)).isEqualTo(Ordering.GT);
    }

    @Test
    public void ord_withMatchStart_shouldComeBeforeAllValues() throws Exception {
        assertThat(IndexValue.<String, Integer>ord().compare(aA, a1)).isEqualTo(Ordering.LT);
        assertThat(IndexValue.<String, Integer>ord().compare(aA, aA)).isEqualTo(Ordering.EQ);
        assertThat(IndexValue.<String, Integer>ord().compare(a1, aA)).isEqualTo(Ordering.GT);
    }

    @Test
    public void ord_withMatchEnd_shouldComeAfterAllValues() throws Exception {
        assertThat(IndexValue.<String, Integer>ord().compare(a2, aZ)).isEqualTo(Ordering.LT);
        assertThat(IndexValue.<String, Integer>ord().compare(aZ, aZ)).isEqualTo(Ordering.EQ);
        assertThat(IndexValue.<String, Integer>ord().compare(aZ, a2)).isEqualTo(Ordering.GT);
    }
}
