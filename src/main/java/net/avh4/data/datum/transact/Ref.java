package net.avh4.data.datum.transact;

public interface Ref<T> {
    T commit(T store);

    T get();
}
