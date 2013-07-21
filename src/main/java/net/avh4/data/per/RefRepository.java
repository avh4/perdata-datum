package net.avh4.data.per;

import com.google.common.collect.ImmutableList;

public class RefRepository {
    private final RefService service;

    public RefRepository(RefService service) {
        this.service = service;
    }

    public <T> ListRef<T> getRef(String refName, Class<T> clazz) {
        return new ListRef<>(this, refName, clazz);
    }

    public <T> void execute(ListRef<T> ref, Transaction<ImmutableList<T>> transaction) {
        String key = getContentKey(ref.name);
        final ImmutableList<T> newItems = transaction.transform(ref.content());
        final String newKey = service.put(newItems);
        service.updateRef(ref.name, key, newKey);
    }

    private String getContentKey(String refName) {
        return service.getContentKey(refName);
    }

    public <T> ImmutableList<T> getList(String refName, Class<T> clazz) {
        final String contentKey = getContentKey(refName);
        if (contentKey == null) return null;
        final Object content = service.getItems(contentKey);
        if (content == null)
            throw new RuntimeException(service.toString() + "did not provide contents for " + contentKey + " which it promised for ref \"" + refName + "\"");
        if (!(content instanceof ImmutableList))
            throw new ClassCastException("Content " + content + " is not an ImmutableList");
        for (java.lang.Object o : (ImmutableList) content) {
            if (!clazz.isInstance(o))
                throw new ClassCastException("Item " + o.toString() + " is not an instance of " + clazz.toString());
        }
        //noinspection unchecked
        return (ImmutableList<T>) content;
    }
}