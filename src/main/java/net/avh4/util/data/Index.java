package net.avh4.util.data;

import java.util.Iterator;

public interface Index<K extends Comparable<K>, V extends Comparable<V>> {

    public Index<K, V> add(K key, V value);

    public Index<K, V> remove(K key, V value);

    public Iterator<IndexEntry<K, V>> iterator(K startKey, K endKey);

    interface IndexEntry<K extends Comparable<K>, V extends Comparable<V>> {
        K key();

        V value();
    }
}
