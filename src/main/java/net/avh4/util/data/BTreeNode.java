package net.avh4.util.data;

public class BTreeNode {
    public final String[] keys;
    public final long[] nodeIds;

    public BTreeNode(String[] keys, long[] nodeIds) {
        this.keys = keys;
        this.nodeIds = nodeIds;
    }
}
