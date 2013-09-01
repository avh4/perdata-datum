package net.avh4.data.per2;

import java.util.HashMap;

public class MemoryDatumStore implements DatumStore {
    private HashMap<String, String> map = new HashMap<String, String>();

    @Override public void write(EntityId entity, String action, String value) {
        map.put(entity.toString() + "-" + action, value);
    }

    @Override public String get(EntityId entityId, String action) {
        return map.get(entityId.toString() + "-" + action);
    }
}
