package net.avh4.data.per;

public interface RefService<T> {
    String getContentKey(String refName);

    T getContent(String contentKey);

    /**
     * @param object the object to store
     * @return the content key of the stored object
     */
    String put(T object);

    /**
     * @param refName           The name of the ref to update.
     * @param currentContentKey The current content key of the ref to update.  If the ref no longer matches this key,
     *                          then the update will fail, and the transaction will need to reapplied.
     * @param newContentKey     The key of the new content for the ref.  The content for this key must already have been stored with #put
     * @throws TransactionException if the ref cannot be atomically changed from currentContentKey to newContentKey
     *                              (meaning some other client has changed the ref).  The caller should read the new current content of the ref
     *                              and reapply the transaction.
     */
    void updateRef(String refName, String currentContentKey, String newContentKey) throws TransactionException;
}
