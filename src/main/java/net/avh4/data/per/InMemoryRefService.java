package net.avh4.data.per;

import net.avh4.data.per.hash.Hasher;
import net.avh4.data.per.hash.MessageDigestHasher;

import java.util.HashMap;

import static com.google.common.base.Preconditions.checkNotNull;

public class InMemoryRefService implements RefService {
    private final HashMap<String, String> refs = new HashMap<>();
    private final HashMap<String, Object> store = new HashMap<>();
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

    @Override public Object getContent(String contentKey) {
        return store.get(contentKey);
    }

    @Override public String put(Object object) {
        String key = hasher.hash(object);
        if (!store.containsKey(key)) {
            store.put(key, object);
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
