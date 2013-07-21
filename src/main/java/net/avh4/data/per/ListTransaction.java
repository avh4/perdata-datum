package net.avh4.data.per;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public abstract class ListTransaction<T> implements Transaction<ImmutableList<T>> {

    @Override public ImmutableList<T> transform(ImmutableList<T> immutableState) {
        ArrayList<T> mutableList;
        if (immutableState == null) {
            mutableList = new ArrayList<>();
        } else {
            mutableList = new ArrayList<>(immutableState);
        }
        mutate(mutableList);
        return ImmutableList.copyOf(mutableList);
    }

    protected abstract void mutate(List<T> mutableList);
}
