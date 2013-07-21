package net.avh4.data.per;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class Ref<T> {

    private final RefRepository repository;
    protected final String name;
    protected final Class<T> clazz;

    protected Ref(RefRepository repository, String name, Class<T> clazz) {
        this.repository = repository;
        this.name = name;
        this.clazz = clazz;
    }

    public ImmutableList<T> content() {
        return repository.getContent(name, clazz);
    }

    public void execute(Transaction<? super List<T>> transaction) {
        repository.execute(this, transaction);
    }
}
