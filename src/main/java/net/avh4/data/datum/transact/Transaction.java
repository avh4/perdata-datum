package net.avh4.data.datum.transact;

import fj.data.List;
import net.avh4.data.datum.prim.Datum;
import net.avh4.data.datum.prim.TempId;

public class Transaction {
    private final List<Datum> assertions;
    private final List<TempId> tempIds;
    private final long nextTempId;
    private final List<Datum> additions;

    public Transaction() {
        this(List.<Datum>nil(), List.<Datum>nil(), List.<TempId>nil(), 0);
    }

    protected Transaction(List<Datum> assertions, List<Datum> additions, List<TempId> tempIds, long nextTempId) {
        this.assertions = assertions;
        this.tempIds = tempIds;
        this.nextTempId = nextTempId;
        this.additions = additions;
    }

    public Transaction set(Datum datum) {
        return new Transaction(
                assertions.cons(datum),
                additions,
                tempIds,
                nextTempId);
    }

    public Transaction add(Datum datum) {
        return new Transaction(
                assertions,
                additions.cons(datum),
                tempIds,
                nextTempId);
    }

    public Iterable<Datum> assertions() {
        return assertions;
    }

    public Iterable<Datum> additions() {
        return additions;
    }
}
