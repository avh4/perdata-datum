package net.avh4.data.datum.store;

import fj.F;
import fj.Ord;
import fj.data.TreeMap;
import net.avh4.data.datum.primitives.TempId;
import net.avh4.util.IteratorIterable;
import net.avh4.util.TransformingIterator;
import net.avh4.util.data.Index;
import net.avh4.util.data.fj.FjSetIndex;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

public class MemoryDatumStore implements DatumStore, Serializable {
    private transient final TreeMap<String, String> map;
    private transient final TreeMap<String, Index<String, String>> indexes;
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

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(map.toMutableMap());
        out.writeLong(nextId);
        out.writeObject(indexes.toMutableMap());
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        try {
            setFinalField("map", TreeMap.fromMutableMap(Ord.stringOrd, (Map) in.readObject()));
            setFinalField("nextId", in.readLong());
            setFinalField("indexes", TreeMap.fromMutableMap(Ord.stringOrd, (Map) in.readObject()));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void setFinalField(String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field f = MemoryDatumStore.class.getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(this, value);
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
        final Index<String, String> index = indexes.get(action).orSome(new FjSetIndex<String, String>());
        final Iterator<Index.IndexEntry<String, String>> itr = index.iterator(startKey, endKey);
        final TransformingIterator<Index.IndexEntry<String, String>, String> itr2 =
                new TransformingIterator<Index.IndexEntry<String, String>, String>(itr, new F<Index.IndexEntry<String, String>, String>() {
                    @Override public String f(Index.IndexEntry<String, String> i) {
                        return i.value();
                    }
                });
        return new IteratorIterable<String>(itr2);
    }

    @Override public DatumStore removeIndex(String action, String value, String entity) {
        Index<String, String> index = indexes.get(action).orSome(new FjSetIndex<String, String>());
        index = index.remove(value, entity);
        final TreeMap<String, Index<String, String>> newIndexes = indexes.set(action, index);
        return new MemoryDatumStore(map, nextId, newIndexes);
    }

    @Override public DatumStore addIndex(String action, String value, String entity) {
        Index<String, String> index = indexes.get(action).orSome(new FjSetIndex<String, String>());
        index = index.add(value, entity);
        final TreeMap<String, Index<String, String>> newIndexes = indexes.set(action, index);
        return new MemoryDatumStore(map, nextId, newIndexes);
    }
}
