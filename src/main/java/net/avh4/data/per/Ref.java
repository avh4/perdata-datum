package net.avh4.data.per;

public abstract class Ref<T> {

    protected final RefRepository repository;
    protected final String name;

    public Ref(RefRepository repository, String name) {
        this.name = name;
        this.repository = repository;
    }

    public abstract T content();

    public abstract void execute(Transaction<T> transaction);
}
