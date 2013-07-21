package net.avh4.data.per.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestHasher implements Hasher {
    private final MessageDigest md;

    /**
     * The provided MessageDigest should not be used in other threads while #hash is being used.
     * You may safely call #hash in multiple threads.
     */
    public MessageDigestHasher(MessageDigest md) {
        this.md = md;
    }

    public static MessageDigestHasher getSha1() {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-1");
            return new MessageDigestHasher(md);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Couldn't create SHA1 hasher", e);
        }
    }

    @Override public synchronized String hash(Object object) {
        md.update(toBytes(object));
        return toHexString(md.digest());
    }

    private static String toHexString(byte[] bytes) {
        final StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    private byte[] toBytes(Object object) {
        if (object instanceof String) {
            return ((String) object).getBytes();
        } else {
            throw new RuntimeException("Don't know how to get digestable bytes for " + object);
        }
    }
}
