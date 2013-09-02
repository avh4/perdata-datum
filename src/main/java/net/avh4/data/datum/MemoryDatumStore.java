package net.avh4.data.datum;

import java.util.HashMap;

public class MemoryDatumStore implements DatumStore {
    private HashMap<String, String> map = new HashMap<String, String>();

    @Override public void write(String entity, String action, String value) {
        map.put(entity + "-" + action, value);
    }

    @Override public String get(String entityId, String action) {
        return map.get(entityId + "-" + action);
    }
}
