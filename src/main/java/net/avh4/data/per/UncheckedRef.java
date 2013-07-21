package net.avh4.data.per;

public class UncheckedRef<T> extends Ref<T> {

    public static class Factory {
        private final RefRepository repository;

        public Factory(RefRepository repository) {
            this.repository = repository;
        }

        public <T> UncheckedRef<T> getRef(String name) {
            return new UncheckedRef<>(repository, name);
        }

        public <T> UncheckedRef<T> getRef(String name, @SuppressWarnings("UnusedParameters") Class<T> clazz) {
            return new UncheckedRef<>(repository, name);
        }
    }

    public UncheckedRef(RefRepository repository, String name) {
        super(repository, name);
    }

    @SuppressWarnings("unchecked")
    @Override public T content() {
        return (T) repository.getContent(name);
    }

    @Override public void execute(Transaction<T> transaction) {
        repository.execute(name, transaction);
    }
}
