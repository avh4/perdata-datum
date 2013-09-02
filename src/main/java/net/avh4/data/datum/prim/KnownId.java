package net.avh4.data.datum.prim;

import net.avh4.data.datum.store.DatumStore;

public class KnownId implements Id {
    private final String id;

    public KnownId(String id) {
        this.id = id;
    }

    @Override public String id() {
        return id;
    }

    @Override public DatumStore resolve(DatumStore store) {
        return store;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KnownId knownId = (KnownId) o;

        if (!id.equals(knownId.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override public String toString() {
        return "#" + id;
    }
}
