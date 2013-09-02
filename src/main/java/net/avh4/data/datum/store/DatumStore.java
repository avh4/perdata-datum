package net.avh4.data.datum.store;

import net.avh4.data.datum.prim.Datum;
import net.avh4.data.datum.prim.Id;
import net.avh4.data.datum.prim.TempId;

public interface DatumStore {
    DatumStore createId(TempId id);

    DatumStore write(Datum datum);

    String get(Id entityId, String action);

    DatumStore add(Datum datum);

    String[] getArray(Id entityId, String action);
}
