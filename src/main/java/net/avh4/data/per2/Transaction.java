package net.avh4.data.per2;

public interface Transaction<T> {
    T run(TransactionContext t);
}
