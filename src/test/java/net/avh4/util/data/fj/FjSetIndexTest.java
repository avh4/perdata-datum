package net.avh4.util.data.fj;

import net.avh4.util.data.Index;
import net.avh4.util.data.IndexContract;

public class FjSetIndexTest extends IndexContract {
    @Override protected <K extends Comparable<K>, V extends Comparable<V>>
    Index<K, V> createSubject(Class<K> keyClass, Class<V> valueClass) {
        return new FjSetIndex<K, V>();
    }
}
