package net.avh4.data.datum.store;

import net.avh4.data.datum.prim.Datum;
import net.avh4.data.datum.prim.Id;
import net.avh4.data.datum.prim.KnownId;
import net.avh4.data.datum.prim.TempId;

import java.util.Iterator;

public interface DatumStore {
    DatumStore createId(TempId id);

    DatumStore set(Datum datum);

    String get(Id entityId, String action);

    DatumStore add(Datum datum);

    String[] getArray(Id entityId, String action);

    Iterable<KnownId> iterate(String index, String startKey, String endKey);
}
