package net.avh4.data.per;

import com.google.common.collect.ImmutableList;

public class RefProvider {
    private final RefService service;

    public RefProvider(RefService service) {
        this.service = service;
    }

    public Ref getRef(String refName) {
        final Ref ref = new Ref();

        final String contentKey = service.getContentKey(refName);
        if (contentKey != null) {
            final ImmutableList items = service.getItems(contentKey);
            ref.setItems(items);
        }
        return ref;
    }
}
