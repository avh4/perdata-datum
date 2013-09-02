package net.avh4.data.datum.store;

import fj.Ord;
import fj.data.TreeMap;
import net.avh4.data.datum.prim.Datum;
import net.avh4.data.datum.prim.Id;
import net.avh4.data.datum.prim.TempId;
import org.json.JSONArray;
import org.json.JSONException;

public class MemoryDatumStore implements DatumStore {
    private final TreeMap<String, String> map;
    private final long nextId;

    public MemoryDatumStore() {
        this(TreeMap.<String, String>empty(Ord.stringOrd), 0);
    }

    protected MemoryDatumStore(TreeMap<String, String> map, long nextId) {
        this.map = map;
        this.nextId = nextId;
    }

    @Override public DatumStore createId(TempId id) {
        long nextId = this.nextId;
        id.mapId("MEM-" + nextId++);
        return new MemoryDatumStore(map, nextId);
    }

    @Override public MemoryDatumStore write(Datum d) {
        final TreeMap<String, String> newMap = map.set(getKey(d), d.value());
        return new MemoryDatumStore(newMap, nextId);
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
            return new MemoryDatumStore(newMap, nextId);
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
