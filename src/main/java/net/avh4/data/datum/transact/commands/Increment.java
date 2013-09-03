package net.avh4.data.datum.transact.commands;

import net.avh4.data.datum.store.DatumStore;
import net.avh4.data.datum.transact.Command;
import net.avh4.data.datum.transact.TransactionException;

public class Increment implements Command {
    private final String entityId;
    private final String action;

    public Increment(String entityId, String action) {
        this.entityId = entityId;
        this.action = action;
    }

    @Override public DatumStore execute(DatumStore store) throws TransactionException {
        String oldValue = store.get(entityId, action);
        int v = oldValue == null ? 0 : Integer.parseInt(oldValue);
        if (oldValue != null) {
            store = store.removeIndex(action, formatIndex(v), entityId);
        }
        v++;
        store = store.set(entityId, action, Integer.toString(v));
        store = store.addIndex(action, formatIndex(v), entityId);
        return store;
    }

    private String formatIndex(int v) {
        return String.format("%010d", v);
    }
}
