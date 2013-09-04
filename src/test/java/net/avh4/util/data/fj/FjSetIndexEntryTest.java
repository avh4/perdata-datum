package net.avh4.util.data.fj;

import fj.Ordering;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class FjSetIndexEntryTest {
    private FjSetIndexEntry<String, Integer> aA = new FjSetIndexEntry<String, Integer>("a", null, false);
    private FjSetIndexEntry<String, Integer> a1 = new FjSetIndexEntry<String, Integer>("a", 1);
    private FjSetIndexEntry<String, Integer> a2 = new FjSetIndexEntry<String, Integer>("a", 2);
    private FjSetIndexEntry<String, Integer> aZ = new FjSetIndexEntry<String, Integer>("a", null, true);
    private FjSetIndexEntry<String, Integer> b1 = new FjSetIndexEntry<String, Integer>("b", 1);

    @Test
    public void ord_shouldOrderDifferingKeys() throws Exception {
        assertThat(FjSetIndexEntry.<String, Integer>ord().compare(a1, b1)).isEqualTo(Ordering.LT);
        assertThat(FjSetIndexEntry.<String, Integer>ord().compare(a1, a1)).isEqualTo(Ordering.EQ);
        assertThat(FjSetIndexEntry.<String, Integer>ord().compare(b1, a1)).isEqualTo(Ordering.GT);
    }

    @Test
    public void ord_shouldOrderDifferingValues() throws Exception {
        assertThat(FjSetIndexEntry.<String, Integer>ord().compare(a1, a2)).isEqualTo(Ordering.LT);
        assertThat(FjSetIndexEntry.<String, Integer>ord().compare(a1, a1)).isEqualTo(Ordering.EQ);
        assertThat(FjSetIndexEntry.<String, Integer>ord().compare(a2, a1)).isEqualTo(Ordering.GT);
    }

    @Test
    public void ord_withMatchStart_shouldComeBeforeAllValues() throws Exception {
        assertThat(FjSetIndexEntry.<String, Integer>ord().compare(aA, a1)).isEqualTo(Ordering.LT);
        assertThat(FjSetIndexEntry.<String, Integer>ord().compare(aA, aA)).isEqualTo(Ordering.EQ);
        assertThat(FjSetIndexEntry.<String, Integer>ord().compare(a1, aA)).isEqualTo(Ordering.GT);
    }

    @Test
    public void ord_withMatchEnd_shouldComeAfterAllValues() throws Exception {
        assertThat(FjSetIndexEntry.<String, Integer>ord().compare(a2, aZ)).isEqualTo(Ordering.LT);
        assertThat(FjSetIndexEntry.<String, Integer>ord().compare(aZ, aZ)).isEqualTo(Ordering.EQ);
        assertThat(FjSetIndexEntry.<String, Integer>ord().compare(aZ, a2)).isEqualTo(Ordering.GT);
    }
}
