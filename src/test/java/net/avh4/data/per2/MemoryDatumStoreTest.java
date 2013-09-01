package net.avh4.data.per2;

public class MemoryDatumStoreTest extends DatumStoreContract {
    @Override protected DatumStore createSubject() {
        return new MemoryDatumStore();
    }
}
