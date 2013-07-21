package net.avh4.data.per;

public interface Transaction<T> {
    public T transform(T immutableState);
}
