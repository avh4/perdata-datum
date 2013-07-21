package net.avh4.data.per;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class RefProvider {
    private final RefService service;

    public RefProvider(RefService service) {
        this.service = service;
    }

    public <T> Ref<T> getRef(String refName, @SuppressWarnings("UnusedParameters") Class<T> clazz) {

        final String contentKey = service.getContentKey(refName);
        if (contentKey != null) {
            final ImmutableList<T> items = service.getItems(contentKey, clazz);
            if (items == null) {
                throw new RuntimeException(service.toString() + "did not provide contents for " + contentKey + " which it promised for ref \"" + refName + "\"");
            }
            return new Ref<>(this, refName, contentKey, items);
        }
        return new Ref<>(this, refName, service.getEmptyListKey(clazz), service.getEmptyList(clazz));
    }

    public <T> void execute(Ref<T> ref, Transaction<? super List<T>> transaction) {
        String key = ref.getContentKey();
        final ArrayList<T> mutableState = new ArrayList<>(ref.items());
        transaction.run(mutableState);
        final ImmutableList<T> newItems = ImmutableList.copyOf(mutableState);
        final String newKey = service.put(newItems);
        ref.setContent(newKey, newItems);
        service.updateRef(ref.getName(), key, newKey);
    }
}
