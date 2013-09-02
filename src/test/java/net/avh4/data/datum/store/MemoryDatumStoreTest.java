package net.avh4.data.datum.store;

public class MemoryDatumStoreTest extends DatumStoreContract {
    @Override protected DatumStore createSubject() {
        return new MemoryDatumStore();
    }
}
