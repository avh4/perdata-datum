package net.avh4.util.data;

import fj.F2;
import fj.Ord;
import fj.Ordering;
import fj.P2;

import static fj.Function.curry;

public class IndexValue<K extends Comparable<K>, V extends Comparable<V>> extends P2<K, V> {
    private final K key;
    private final V value;
    private final boolean matchEnd;

    public IndexValue(K key, V value) {
        this.key = key;
        this.value = value;
        this.matchEnd = false;
    }

    public IndexValue(K key, Void value, boolean matchEnd) {
        this.key = key;
        this.value = null;
        this.matchEnd = matchEnd;
    }

    public static <K extends Comparable<K>, V extends Comparable<V>> Ord<IndexValue<K, V>> ord() {
        final Ord<K> oa = Ord.comparableOrd();
        return Ord.ord(curry(new F2<IndexValue<K, V>, IndexValue<K, V>, Ordering>() {
            public Ordering f(final IndexValue<K, V> a, final IndexValue<K, V> b) {
                if (!oa.eq(a.key, b.key)) return oa.compare(a.key, b.key);
                else {
                    if (a.value != null && b.value != null) return Ord.<V>comparableOrd().compare(a.value, b.value);
                    else if (a.matchEnd && b.matchEnd) return Ordering.EQ;
                    else if (a.matchEnd) return Ordering.GT;
                    else if (b.matchEnd) return Ordering.LT;
                    else if (a.value != null) return Ordering.GT;
                    else if (b.value != null) return Ordering.LT;
                    else return Ordering.EQ;
                }
            }
        }));
    }

    public K key() {
        return key;
    }

    public V value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndexValue that = (IndexValue) o;

        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override public String toString() {
        return "<" + key + ":" + value + '>';
    }

    @Override public K _1() {
        return key;
    }

    @Override public V _2() {
        return value;
    }
}
