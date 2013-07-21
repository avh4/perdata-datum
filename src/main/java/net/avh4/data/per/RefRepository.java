package net.avh4.data.per;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RefRepository {
    private final RefService service;

    public RefRepository(RefService service) {
        this.service = service;
    }

    public <T> Ref<T> getRef(String refName, Class<T> clazz) {
        return new Ref<>(this, refName, clazz);
    }

    public <T> void execute(Ref<T> ref, Transaction<? super List<T>> transaction) {
        String key = getContentKey(ref.name, ref.clazz);
        final ArrayList<T> mutableState = new ArrayList<>(ref.content());
        transaction.run(mutableState);
        final ImmutableList<T> newItems = ImmutableList.copyOf(mutableState);
        final String newKey = service.put(newItems);
        service.updateRef(ref.name, key, newKey);
    }

    private @NotNull String getContentKey(String refName, Class<?> clazz) {
        final String contentKey = service.getContentKey(refName);
        if (contentKey == null) return service.getEmptyListKey(clazz);
        return contentKey;
    }

    public <T> ImmutableList<T> getContent(String refName, Class<T> clazz) {
        final String contentKey = getContentKey(refName, clazz);
        final ImmutableList<T> content = service.getItems(contentKey, clazz);
        if (content == null)
            throw new RuntimeException(service.toString() + "did not provide contents for " + contentKey + " which it promised for ref \"" + refName + "\"");
        return content;
    }
}
