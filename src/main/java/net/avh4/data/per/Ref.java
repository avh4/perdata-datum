package net.avh4.data.per;

import com.google.common.collect.ImmutableList;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class Ref {

    private ImmutableList<?> items;

    public Ref() {
        items = ImmutableList.of();
    }

    public List<?> items() {
        return items;
    }

    protected void setItems(ImmutableList items) {
        checkNotNull(items);
        this.items = items;
    }
}
