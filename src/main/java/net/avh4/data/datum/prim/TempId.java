package net.avh4.data.datum.prim;

import net.avh4.data.datum.store.DatumStore;

public class TempId implements Id {
    private String realId;

    public String id() {
        if (realId == null) throw new IllegalStateException("TempId is not mapped: " + toString());
        return realId;
    }

    @Override public DatumStore resolve(DatumStore store) {
        if (this.realId == null) return store.createId(this);
        else return store;
    }

    public void mapId(String id) {
        if (this.realId == null) this.realId = id;
        else throw new IllegalStateException("TempId is already mapped: " + toString());
    }

    @Override public String toString() {
        if (realId == null) return super.toString() + "(not mapped)";
        else return "#(" + realId + ')';
    }
}
