package net.avh4.data.per.serialize;

import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

public class SerializableSerializer implements Serializer<Serializable> {
    @Override public byte[] serializeToArray(Serializable object) {
        return SerializationUtils.serialize(object);
    }

    @Override public Serializable deserialize(byte[] bytes) {
        return (Serializable) SerializationUtils.deserialize(bytes);
    }
}
