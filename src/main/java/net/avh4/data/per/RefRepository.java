package net.avh4.data.per;

public class RefRepository {
    private final RefService service;

    public RefRepository(RefService service) {
        this.service = service;
    }

    public <T> ListRef<T> getRef(String refName, Class<T> clazz) {
        return new ListRef<>(this, refName, clazz);
    }

    public void execute(String refName, Transaction transaction) {
        final String key = getContentKey(refName);
        final Object content = getContent(refName);
        final Object newContent = transaction.transform(content);
        final String newKey = service.put(newContent);
        service.updateRef(refName, key, newKey);
    }

    private String getContentKey(String refName) {
        return service.getContentKey(refName);
    }

    public Object getContent(String refName) {
        final String contentKey = getContentKey(refName);
        if (contentKey == null) return null;
        final Object content = service.getItems(contentKey);
        if (content == null)
            throw new RuntimeException(service.toString() + "did not provide contents for " + contentKey + " which it promised for ref \"" + refName + "\"");
        return content;
    }
}
