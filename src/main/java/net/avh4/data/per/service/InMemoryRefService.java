package net.avh4.data.per.service;

public class InMemoryRefService extends SerializableRefService {
    public InMemoryRefService() {
        super(new InMemoryStorage<byte[]>());
    }
}
