package net.avh4.data.per;

import net.avh4.data.per.hash.Hasher;
import net.avh4.data.per.hash.MessageDigestHasher;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.HashMap;

import static com.google.common.base.Preconditions.checkNotNull;

public class InMemoryRefService implements RefService<Serializable> {
    private final HashMap<String, String> refs = new HashMap<>();
    private final HashMap<String, byte[]> store = new HashMap<>();
    private final Hasher hasher;

    public InMemoryRefService() {
        this(MessageDigestHasher.getSha1());
    }

    public InMemoryRefService(Hasher hasher) {
        this.hasher = hasher;
    }

    @Override public String getContentKey(String refName) {
        return refs.get(refName);
    }

    @Override public Serializable getContent(String contentKey) {
        final byte[] bytes = store.get(contentKey);
        return (Serializable) SerializationUtils.deserialize(bytes);
    }

    @Override public String put(Serializable object) {
        String key = hasher.hash(object);
        if (!store.containsKey(key)) {
            final byte[] bytes = SerializationUtils.serialize(object);
            store.put(key, bytes);
        }
        return key;
    }

    @Override
    public void updateRef(String refName, String currentContentKey, String newContentKey) throws TransactionException {
        checkNotNull(refName);
        if (newContentKey != null && !store.containsKey(newContentKey)) {
            throw new RuntimeException("Content for " + newContentKey + " must be added with put before calling updateRef");
        }
        synchronized (this) {
            final String actualContentKey = refs.get(refName);
            if (currentContentKey == null && actualContentKey != null) {
                throw new TransactionException("Expected to create ref \"" + refName + "\" as " + newContentKey + ", but it already exists (with content key " + actualContentKey + ")");
            }
            if (currentContentKey != null && !currentContentKey.equals(refs.get(refName))) {
                throw new TransactionException("Expected to update ref \"" + refName + "\" from " + currentContentKey + " to " + newContentKey + ", but it was " + actualContentKey);
            }
            refs.put(refName, newContentKey);
        }
    }
}
