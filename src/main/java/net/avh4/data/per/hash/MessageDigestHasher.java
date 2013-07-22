package net.avh4.data.per.hash;

import net.avh4.data.per.serialize.SerializableSerializer;
import net.avh4.data.per.serialize.Serializer;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestHasher<T> implements Hasher<T> {
    private final MessageDigest md;
    private final Serializer<T, byte[]> serializer;

    /**
     * The provided MessageDigest should not be used in other threads while #hash is being used.
     * You may safely call #hash in multiple threads.
     */
    public MessageDigestHasher(MessageDigest md, Serializer<T, byte[]> serializer) {
        this.md = md;
        this.serializer = serializer;
    }

    public static MessageDigestHasher<Serializable> getSha1() {
        return getSha1(new SerializableSerializer());
    }

    public static <T> MessageDigestHasher<T> getSha1(Serializer<T, byte[]> serializer) {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-1");
            return new MessageDigestHasher<T>(md, serializer);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Couldn't create SHA1 hasher", e);
        }
    }

    @Override public synchronized String hash(T object) {
        final byte[] bytes = serializer.serializeToArray(object);
        md.update(bytes);
        return toHexString(md.digest());
    }

    private static String toHexString(byte[] bytes) {
        final StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
