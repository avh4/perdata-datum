package net.avh4.data.datum;

public class MemoryDatumStoreTest extends DatumStoreContract {
    @Override protected DatumStore createSubject() {
        return new MemoryDatumStore();
    }
}
