package net.avh4.data.datum.transact.commands;

import net.avh4.data.datum.store.DatumStore;
import net.avh4.data.datum.transact.Command;
import net.avh4.data.datum.transact.IndexStore;
import net.avh4.data.datum.transact.KeyStore;
import net.avh4.data.datum.transact.TransactionException;

public class Set implements Command {
    private final String entity;
    private final String action;
    private final String value;

    public Set(String entity, String action, String value) {
        this.entity = entity;
        this.action = action;
        this.value = value;
    }

    @Override public DatumStore execute(DatumStore store) throws TransactionException {
        store = store.set(entity, action, value);
        String oldValue = store.get(entity, action);
        if (oldValue != null) {
            store = store.removeIndex(action, oldValue, entity);
        }
        store = store.addIndex(action, value, entity);
        return store;
    }
}
