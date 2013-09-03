package net.avh4.data.datum.store;

import fj.F;
import fj.Ord;
import fj.data.TreeMap;
import net.avh4.data.datum.prim.TempId;
import net.avh4.util.IteratorIterable;
import net.avh4.util.TransformingIterator;
import net.avh4.util.data.DefaultIndex;
import net.avh4.util.data.Index;
import net.avh4.util.data.IndexValue;

import java.util.Iterator;

public class MemoryDatumStore implements DatumStore {
    private final TreeMap<String, String> map;
    private final TreeMap<String, Index<String, String>> indexes;
    private final long nextId;

    public MemoryDatumStore() {
        this(TreeMap.<String, String>empty(Ord.stringOrd),
                0,
                TreeMap.<String, Index<String, String>>empty(Ord.stringOrd));
    }

    protected MemoryDatumStore(TreeMap<String, String> map, long nextId, TreeMap<String, Index<String, String>> indexes) {
        this.map = map;
        this.nextId = nextId;
        this.indexes = indexes;
    }

    @Override public DatumStore createId(TempId id) {
        long nextId = this.nextId;
        id.mapId("MEM-" + nextId++);
        return new MemoryDatumStore(map, nextId, indexes);
    }

    @Override public DatumStore set(String entityId, String action, String value) {
        final TreeMap<String, String> newMap = map.set(entityId + "-" + action, value);
        return new MemoryDatumStore(newMap, nextId, indexes);
    }

    @Override public String get(String entityId, String action) {
        return map.get(entityId + "-" + action).toNull();
    }

    @Override public Iterable<String> iterate(String action, final String startKey, final String endKey) {
        final Index<String, String> index = indexes.get(action).orSome(new DefaultIndex<String, String>());
        final Iterator<IndexValue<String, String>> itr = index.iterator(startKey, endKey);
        final TransformingIterator<IndexValue<String, String>, String> itr2 =
                new TransformingIterator<IndexValue<String, String>, String>(itr, new F<IndexValue<String, String>, String>() {
                    @Override public String f(IndexValue<String, String> i) {
                        return i.value();
                    }
                });
        return new IteratorIterable<String>(itr2);
    }

    @Override public DatumStore removeIndex(String action, String value, String entity) {
        Index<String, String> index = indexes.get(action).orSome(new DefaultIndex<String, String>());
        index = index.remove(value, entity);
        final TreeMap<String, Index<String, String>> newIndexes = indexes.set(action, index);
        return new MemoryDatumStore(map, nextId, newIndexes);
    }

    @Override public DatumStore addIndex(String action, String value, String entity) {
        Index<String, String> index = indexes.get(action).orSome(new DefaultIndex<String, String>());
        index = index.add(value, entity);
        final TreeMap<String, Index<String, String>> newIndexes = indexes.set(action, index);
        return new MemoryDatumStore(map, nextId, newIndexes);
    }
}
