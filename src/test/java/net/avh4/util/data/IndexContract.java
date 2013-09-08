package net.avh4.util.data;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.fest.assertions.Assertions.assertThat;

public abstract class IndexContract {
    private Index<String, String> subject;

    protected abstract <K extends Comparable<K>, V extends Comparable<V>>
    Index<K, V> createSubject(Class<K> keyClass, Class<V> valueClass);

    @Before
    public void setUp() throws Exception {
        subject = createSubject(String.class, String.class);
    }

    @Test
    public void iteratesSingleValue() throws Exception {
        subject = subject.add("Arnold", "Arnold Palmer");
        assertThat(subject.iterator("Arnold", "Arnold")).hasSize(1);
        assertThat(subject.iterator("Arnold", "Arnold").next().value()).isEqualTo("Arnold Palmer");
    }

    @Test
    public void iteratesSkipsRemovedValues() throws Exception {
        subject = subject.add("Arnold", "Arnold Palmer");
        subject = subject.remove("Arnold", "Arnold Palmer");
        assertThat(subject.iterator("Arnold", "Arnold")).isEmpty();
    }

    @Test
    public void iterator_skipsToMatchingValues() throws Exception {
        subject = subject.add("Arnold", "Arnold Palmer").add("Betty", "Betty White");
        assertThat(subject.iterator("Betty", "Betty")).hasSize(1);
        assertThat(subject.iterator("Betty", "Betty").next().value()).isEqualTo("Betty White");
    }

    @Test
    public void iterator_stopsAfterMatchingValues() throws Exception {
        subject = subject.add("Arnold", "Arnold Palmer").add("Betty", "Betty White");
        assertThat(subject.iterator("Arnold", "Arnold")).hasSize(1);
        assertThat(subject.iterator("Arnold", "Arnold").next().value()).isEqualTo("Arnold Palmer");
    }

    @Test
    public void iterator_iteratesInAscendingOrder() throws Exception {
        subject = subject.add("Arnold", "Arnold Palmer").add("Betty", "Betty White");
        final Iterator<Index.IndexEntry<String, String>> itr = subject.iterator(null, null);
        assertThat(itr.next().value()).isEqualTo("Arnold Palmer");
        assertThat(itr.next().value()).isEqualTo("Betty White");
    }
}
