package net.avh4.util.data;

import org.junit.Before;
import org.junit.Test;

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
        assertThat(subject.iterator("Arnold", "Arnold")).containsOnly(new IndexValue("Arnold", "Arnold Palmer"));
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
        assertThat(subject.iterator("Betty", "Betty")).containsOnly(new IndexValue("Betty", "Betty White"));
    }

    @Test
    public void iterator_stopsAfterMatchingValues() throws Exception {
        subject = subject.add("Arnold", "Arnold Palmer").add("Betty", "Betty White");
        assertThat(subject.iterator("Arnold", "Arnold")).containsOnly(new IndexValue("Arnold", "Arnold Palmer"));
    }
}
