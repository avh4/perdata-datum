package net.avh4.data.datum;

public interface Transaction<T> {
    T run(TransactionContext t);
}
