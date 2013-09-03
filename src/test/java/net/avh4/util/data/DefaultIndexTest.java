package net.avh4.util.data;

public class DefaultIndexTest extends IndexContract {
    @Override protected <K extends Comparable<K>, V extends Comparable<V>>
    Index<K, V> createSubject(Class<K> keyClass, Class<V> valueClass) {
        return new DefaultIndex<K, V>();
    }
}
