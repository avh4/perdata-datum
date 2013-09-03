package net.avh4.util.data;

import fj.P3;
import fj.data.Option;
import fj.data.Set;

import java.util.Iterator;

public class DefaultIndex<K extends Comparable<K>, V extends Comparable<V>> implements Index<K, V> {
    final Set<IndexValue<K, V>> set;

    public DefaultIndex() {
        this(Set.empty(IndexValue.<K, V>ord()));
    }

    protected DefaultIndex(Set<IndexValue<K, V>> set) {
        this.set = set;
    }

    @Override public DefaultIndex<K, V> add(K key, V value) {
        final IndexValue<K, V> iv = new IndexValue<K, V>(key, value);
        final Set<IndexValue<K, V>> newSet = set.insert(iv);
        return new DefaultIndex<K, V>(newSet);
    }

    @Override public DefaultIndex<K, V> remove(K key, V value) {
        return this;
    }

    @Override public Iterator<IndexValue<K, V>> iterator(K startKey, K endKey) {
        final IndexValue<K, V> start = new IndexValue<K,V>(startKey, null, false);
        final IndexValue<K, V> end = new IndexValue<K,V>(endKey, null, true);

        final Set<IndexValue<K, V>> skipPre = set.split(start)._3();
        final P3<Set<IndexValue<K, V>>, Option<IndexValue<K, V>>, Set<IndexValue<K, V>>> split
                = skipPre.split(end);
        Set<IndexValue<K, V>> matches = split._1();
        if (split._2().isSome()) matches = matches.insert(split._2().some());

        return matches.iterator();
    }
}
