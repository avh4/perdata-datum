package net.avh4.data.datum.store;

import net.avh4.data.datum.primitives.TempId;

public interface DatumStore {
    DatumStore createId(TempId id);

    DatumStore set(String entityId, String action, String value);

    String get(String entityId, String action);

    DatumStore addIndex(String action, String value, String entity);

    DatumStore removeIndex(String action, String value, String entityId);

    Iterable<String> iterate(String index, String startKey, String endKey);
}
