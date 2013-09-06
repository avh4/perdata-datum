package net.avh4.data.datum.transact;

public class InMemoryRef<T> implements Ref<T> {
    private T value;

    public InMemoryRef(T initial) {
        this.value = initial;
    }

    @Override public T commit(T newValue) {
        value = newValue;
        return newValue;
    }

    @Override public T get() {
        return value;
    }
}
