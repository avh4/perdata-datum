package net.avh4.data.datum.peer;

import net.avh4.data.datum.primitives.KnownId;
import net.avh4.data.datum.store.DatumStore;

import java.util.ArrayList;

public class Query {
    private final String index;
    private final String value;

    public Query(String index, String value) {
        this.index = index;
        this.value = value;
    }

    public Query(String index) {
        this.index = index;
        this.value = null;
    }

    public KnownId[] execute(DatumStore store) {
        ArrayList<KnownId> matches = new ArrayList<KnownId>();
        for (String id : store.iterate(index, value, value)) {
            matches.add(new KnownId(id));
        }
        return matches.toArray(new KnownId[matches.size()]);
    }
}
