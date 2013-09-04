package net.avh4.util.data;

public interface BTreeStorage {
    public long write(BTree content);

    BTree get(long node);
}
