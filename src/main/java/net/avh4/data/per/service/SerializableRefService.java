package net.avh4.data.per.service;

import net.avh4.data.per.hash.Hasher;
import net.avh4.data.per.hash.MessageDigestHasher;
import net.avh4.data.per.serialize.SerializableSerializer;
import net.avh4.data.per.serialize.Serializer;

import java.io.Serializable;

public abstract class SerializableRefService extends DefaultRefService<Serializable, byte[]> {

    public SerializableRefService(Storage<byte[]> storage) {
        this(MessageDigestHasher.getSha1(), storage);
    }

    public SerializableRefService(Hasher<Serializable> hasher, Storage<byte[]> storage) {
        this(hasher, new SerializableSerializer(), storage);
    }

    public SerializableRefService(Hasher<Serializable> hasher, Serializer<Serializable, byte[]> serializer, Storage<byte[]> storage) {
        super(hasher, serializer, storage);
    }

}
