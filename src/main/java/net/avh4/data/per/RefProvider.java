package net.avh4.data.per;

import com.google.common.collect.ImmutableList;

public class RefProvider {
    private final RefService service;

    public RefProvider(RefService service) {
        this.service = service;
    }

    public <T> Ref<T> getRef(String refName, @SuppressWarnings("UnusedParameters") Class<T> clazz) {
        final Ref<T> ref = new Ref<>();

        final String contentKey = service.getContentKey(refName);
        if (contentKey != null) {
            final ImmutableList<T> items = service.getItems(contentKey, clazz);
            if (items == null) {
                throw new RuntimeException(service.toString() + "did not provide contents for " + contentKey + " which it promised for ref \"" + refName + "\"");
            }
            ref.setItems(items);
        }
        return ref;
    }
}
