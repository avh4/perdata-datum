package net.avh4.data.per;

import com.google.common.collect.ImmutableList;

public class ListRef<T> {

    private final RefRepository repository;
    protected final String name;
    protected final Class<T> clazz;

    protected ListRef(RefRepository repository, String name, Class<T> clazz) {
        this.repository = repository;
        this.name = name;
        this.clazz = clazz;
    }

    public ImmutableList<T> content() {
        return repository.getList(name, clazz);
    }

    public void execute(Transaction<ImmutableList<T>> transaction) {
        repository.execute(this, transaction);
    }
}
