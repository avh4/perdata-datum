package net.avh4.data.datum.store.service;

public class InMemoryRefTest extends RefContract {

    protected Ref<String> createSubject(String initialValue) {
        return new InMemoryRef<String>(initialValue);
    }
}
