package net.avh4.data.per;

public interface RefService {
    String getContentKey(String refName);

    Object getItems(String contentKey);

    /**
     * @param object the object to store
     * @return the content key of the stored object
     */
    String put(Object object);

    /**
     * @param refName           The name of the ref to update.
     * @param currentContentKey The current content key of the ref to update.  If the ref no longer matches this key,
     *                          then the update will fail, and the transaction will need to reapplied.
     * @param newContentKey     The key of the new content for the ref.  The content for this key must already have been stored with #put
     */
    void updateRef(String refName, String currentContentKey, String newContentKey);
}
