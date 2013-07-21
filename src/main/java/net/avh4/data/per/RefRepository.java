package net.avh4.data.per;

import com.google.common.collect.ImmutableList;

public class RefRepository {
    private final RefService service;

    public RefRepository(RefService service) {
        this.service = service;
    }

    public <T> Ref<T> getRef(String refName, Class<T> clazz) {
        return new Ref<>(this, refName, clazz);
    }

    public <T> void execute(Ref<T> ref, Transaction<ImmutableList<T>> transaction) {
        String key = getContentKey(ref.name);
        final ImmutableList<T> newItems = transaction.transform(ref.content());
        final String newKey = service.put(newItems);
        service.updateRef(ref.name, key, newKey);
    }

    private String getContentKey(String refName) {
        return service.getContentKey(refName);
    }

    public <T> ImmutableList<T> getContent(String refName, Class<T> clazz) {
        final String contentKey = getContentKey(refName);
        if (contentKey == null) return null;
        final ImmutableList<T> content = service.getItems(contentKey, clazz);
        if (content == null)
            throw new RuntimeException(service.toString() + "did not provide contents for " + contentKey + " which it promised for ref \"" + refName + "\"");
        return content;
    }
}
