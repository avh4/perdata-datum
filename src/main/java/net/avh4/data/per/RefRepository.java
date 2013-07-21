package net.avh4.data.per;

public class RefRepository<T> {
    private final RefService<T> service;

    public RefRepository(RefService<T> service) {
        this.service = service;
    }

    public void execute(String refName, Transaction<T> transaction) {
        boolean success = false;
        int attempts = 0;
        do {
            final String key = getContentKey(refName);
            final T content = getContent(refName);
            final T newContent = transaction.transform(content);
            final String newKey = service.put(newContent);
            try {
                service.updateRef(refName, key, newKey);
                success = true;
            } catch (TransactionException e) {
                attempts++;
                if (attempts > 10) {
                    throw new RuntimeException("Transaction for \"" + refName + "\" failed 10 times, aborting: " + transaction, e);
                }
            }
        }
        while (!success);
    }

    private String getContentKey(String refName) {
        return service.getContentKey(refName);
    }

    public T getContent(String refName) {
        final String contentKey = getContentKey(refName);
        if (contentKey == null) return null;
        final T content = service.getContent(contentKey);
        if (content == null)
            throw new RuntimeException(service.toString() + "did not provide contents for " + contentKey + " which it promised for ref \"" + refName + "\"");
        return content;
    }
}
