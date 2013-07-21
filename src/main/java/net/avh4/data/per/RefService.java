package net.avh4.data.per;

import com.google.common.collect.ImmutableList;

public interface RefService {
    String getContentKey(String refName);

    <T> ImmutableList<T> getItems(String hash, Class<T> clazz);
}
