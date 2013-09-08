package net.avh4.util.data.fj;

import fj.Ord;
import net.avh4.util.data.Index;

import java.io.Serializable;

class FjSetIndexEntry<K extends Comparable<K>, V extends Comparable<V>>
        implements Index.IndexEntry<K, V>, Comparable<FjSetIndexEntry<K, V>>, Serializable {

    private static final long serialVersionUID = -3671914170699792093L;

    private final K key;
    private final V value;
    private final boolean matchEnd;

    public FjSetIndexEntry(K key, V value) {
        this.key = key;
        this.value = value;
        this.matchEnd = false;
    }

    public FjSetIndexEntry(K key, @SuppressWarnings("UnusedParameters") Void value, boolean matchEnd) {
        this.key = key;
        this.value = null;
        this.matchEnd = matchEnd;
    }

    public static <K extends Comparable<K>, V extends Comparable<V>> Ord<FjSetIndexEntry<K, V>> ord() {
        return Ord.comparableOrd();
    }

    @Override public K key() {
        return key;
    }

    @Override public V value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FjSetIndexEntry that = (FjSetIndexEntry) o;

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

    @Override public int compareTo(FjSetIndexEntry<K, V> b) {
        if (key == null && b.key == null && matchEnd == b.matchEnd) return 0;
        if (key == null && matchEnd) return 1;
        if (b.key == null && b.matchEnd) return -1;
        if (key == null) return -1;
        if (b.key == null) return 1;
        if (!key.equals(b.key)) return key.compareTo(b.key);
        if (value != null && b.value != null) return value.compareTo(b.value);
        if (matchEnd && b.matchEnd) return 0;
        if (matchEnd) return 1;
        if (b.matchEnd) return -1;
        if (value != null) return 1;
        if (b.value != null) return -1;
        return 0;
    }
}
