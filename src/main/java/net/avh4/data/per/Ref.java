package net.avh4.data.per;

import com.google.common.collect.ImmutableList;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class Ref<T> {

    private ImmutableList<T> items;

    public Ref() {
        items = ImmutableList.of();
    }

    public List<T> items() {
        return items;
    }

    protected void setItems(ImmutableList<T> items) {
        checkNotNull(items);
        this.items = items;
    }
}
