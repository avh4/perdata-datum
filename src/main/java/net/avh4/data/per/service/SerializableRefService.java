package net.avh4.data.per.service;

import net.avh4.data.per.hash.Hasher;
import net.avh4.data.per.hash.MessageDigestHasher;
import net.avh4.data.per.serialize.SerializableSerializer;
import net.avh4.data.per.serialize.Serializer;

import java.io.Serializable;

public abstract class SerializableRefService extends DefaultRefService<Serializable> {

    public SerializableRefService(Storage storage) {
        this(MessageDigestHasher.getSha1(), storage);
    }

    public SerializableRefService(Hasher<Serializable> hasher, Storage storage) {
        this(hasher, new SerializableSerializer(), storage);
    }

    public SerializableRefService(Hasher<Serializable> hasher, Serializer<Serializable> serializer, Storage storage) {
        super(hasher, serializer, storage);
    }

}
