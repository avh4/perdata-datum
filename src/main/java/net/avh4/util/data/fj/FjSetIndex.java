package net.avh4.util.data.fj;

import fj.P3;
import fj.data.Option;
import fj.data.Set;
import net.avh4.util.data.Index;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Iterator;

public class FjSetIndex<K extends Comparable<K>, V extends Comparable<V>> implements Index<K, V>, Serializable {
    private final Set<FjSetIndexEntry<K, V>> set;

    public FjSetIndex() {
        this(Set.empty(FjSetIndexEntry.<K, V>ord()));
    }

    protected FjSetIndex(Set<FjSetIndexEntry<K, V>> set) {
        this.set = set;
    }

    @SuppressWarnings("unchecked")
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(((Set) set).toStream().toArray(FjSetIndexEntry[].class).array());
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        try {
            setFinalField("set", Set.set(FjSetIndexEntry.ord(), (FjSetIndexEntry[]) in.readObject()));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void setFinalField(String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field f = FjSetIndex.class.getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(this, value);
    }

    @Override public FjSetIndex<K, V> add(K key, V value) {
        final FjSetIndexEntry<K, V> iv = new FjSetIndexEntry<K, V>(key, value);
        final Set<FjSetIndexEntry<K, V>> newSet = set.insert(iv);
        return new FjSetIndex<K, V>(newSet);
    }

    @Override public FjSetIndex<K, V> remove(K key, V value) {
        final FjSetIndexEntry<K, V> iv = new FjSetIndexEntry<K, V>(key, value);
        final Set<FjSetIndexEntry<K, V>> newSet = set.delete(iv);
        return new FjSetIndex<K, V>(newSet);
    }

    @Override public Iterator<IndexEntry<K, V>> iterator(K startKey, K endKey) {
        final FjSetIndexEntry<K, V> start = new FjSetIndexEntry<K, V>(startKey, null, false);
        final FjSetIndexEntry<K, V> end = new FjSetIndexEntry<K, V>(endKey, null, true);

        final Set<FjSetIndexEntry<K, V>> skipPre = set.split(start)._3();
        final P3<Set<FjSetIndexEntry<K, V>>, Option<FjSetIndexEntry<K, V>>, Set<FjSetIndexEntry<K, V>>> split
                = skipPre.split(end);
        Set<FjSetIndexEntry<K, V>> matches = split._1();
        if (split._2().isSome()) matches = matches.insert(split._2().some());

        //noinspection unchecked
        return (Iterator<IndexEntry<K, V>>) (Iterator<? extends IndexEntry<K, V>>) matches.iterator();
    }

    @Override public String toString() {
        return "FjSetIndex:" + set.toList();
    }
}
