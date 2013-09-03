package net.avh4.data.datum.store;

import fj.F;
import fj.Ord;
import fj.data.TreeMap;
import net.avh4.data.datum.prim.Datum;
import net.avh4.data.datum.prim.Id;
import net.avh4.data.datum.prim.KnownId;
import net.avh4.data.datum.prim.TempId;
import net.avh4.util.IteratorIterable;
import net.avh4.util.TransformingIterator;
import net.avh4.util.data.DefaultIndex;
import net.avh4.util.data.Index;
import net.avh4.util.data.IndexValue;
import org.json.JSONArray;
import org.json.JSONException;

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

    @Override public MemoryDatumStore set(Datum d) {
        final TreeMap<String, String> newMap = map.set(getKey(d), d.value());
        Index<String, String> index = indexes.get(d.action()).orSome(new DefaultIndex<String, String>());
        index = index.add(d.value(), d.entityId());
        return new MemoryDatumStore(newMap, nextId, indexes.set(d.action(), index));
    }

    @Override public String get(Id entityId, String action) {
        return map.get(entityId.id() + "-" + action).toNull();
    }

    @Override public MemoryDatumStore add(Datum d) {
        try {
            final String key = getKey(d);
            String currentValue = map.get(key).orSome("[]");
            JSONArray a = new JSONArray(currentValue);
            a.put(d.value());
            final TreeMap<String, String> newMap = map.set(key, a.toString());
            return new MemoryDatumStore(newMap, nextId, indexes);
        } catch (JSONException e) {
            throw new RuntimeException("Internal error", e);
        }
    }

    @Override public String[] getArray(Id entityId, String action) {
        try {
            return jsonToArray(get(entityId, action));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override public Iterable<KnownId> iterate(String action, final String startKey, final String endKey) {
        final Index<String, String> index = indexes.get(action).orSome(new DefaultIndex<String, String>());
        final Iterator<IndexValue<String, String>> itr = index.iterator(startKey, endKey);
        final TransformingIterator<IndexValue<String, String>, KnownId> itr2 =
                new TransformingIterator<IndexValue<String, String>, KnownId>(itr, new F<IndexValue<String, String>, KnownId>() {
            @Override public KnownId f(IndexValue<String, String> i) {
                return new KnownId(i.value());
            }
        });
        return new IteratorIterable<KnownId>(itr2);
    }

    private String getKey(Datum d) {
        return d.entityId() + "-" + d.action();
    }

    private String[] jsonToArray(String json) throws JSONException {
        if (json == null) return new String[]{};
        final JSONArray jsonArray = new JSONArray(json);
        final String[] a = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            a[i] = jsonArray.getString(i);
        }
        return a;
    }
}
