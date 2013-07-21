package net.avh4.data.per;

@SuppressWarnings("UnusedDeclaration")
public class StdoutLoggingRefServiceWrapper implements RefService {
    private final RefService inner;

    public StdoutLoggingRefServiceWrapper(RefService inner) {
        this.inner = inner;
    }

    @Override public String getContentKey(String refName) {
        return inner.getContentKey(refName);
    }

    @Override public Object getContent(String contentKey) {
        return inner.getContent(contentKey);
    }

    @Override public String put(Object object) {
        final String key = inner.put(object);
        log("Put for " + key + ": " + object.toString());
        return key;
    }

    @Override
    public void updateRef(String refName, String currentContentKey, String newContentKey) throws TransactionException {
        try {
            inner.updateRef(refName, currentContentKey, newContentKey);
            log("Set ref \"" + refName + "\": " + currentContentKey + " -> " + newContentKey);
        } catch (Throwable e) {
            log("Set ref \"" + refName + "\": !! " + e.getLocalizedMessage());
            throw e;
        }
    }

    private void log(String message) {
        System.out.println(message);
    }
}