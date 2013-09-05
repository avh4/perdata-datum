package net.avh4.util.data;

public class BTree {
    private final BTreeStorage storage;
    public final String[] keys;
    public final long[] nodes;
    public final String[] vals;

    public BTree(BTreeStorage storage, int d) {
        this.storage = storage;
        keys = new String[d * 2];
        nodes = null;
        vals = new String[d * 2];
    }

    protected BTree(BTreeStorage storage, String[] keys, String[] vals) {
        this.storage = storage;
        this.keys = keys;
        this.nodes = null;
        this.vals = vals;
    }

    protected BTree(BTreeStorage storage, String[] keys, long[] nodes) {
        this.storage = storage;
        this.keys = keys;
        this.nodes = nodes;
        this.vals = null;
    }

    public BTree insert(String key, String value) {
        if (vals == null) {
            return insertInBranch(key, value);
        } else {
            return insertOrSplitLeaf(key, value);
        }
    }

    private BTree insertOrSplitLeaf(String key, String value) {
        if (keys[keys.length - 1] == null) {
            return insertInLeaf(key, value);
        } else {
            return splitLeaf(key, value);
        }
    }

    private BTree insertInLeaf(String key, String value) {
        final String[] keys = this.keys.clone();
        final String[] vals = this.vals.clone();
        int i;
        for (i = keys.length - 1; i > 0; i--) {
            if (keys[i - 1] == null) continue;
            if (key.compareTo(keys[i - 1]) >= 0) break;
            keys[i] = keys[i - 1];
            vals[i] = vals[i - 1];
        }
        keys[i] = key;
        vals[i] = value;
        return new BTree(storage, keys, vals);
    }

    private BTree insertInBranch(String key, String value) {
        int i;
        for (i = keys.length; i > 0; i--) {
            if (keys[i - 1] == null) continue;
            if (key.compareTo(keys[i-1]) >= 0) break;
        }
        BTree result = storage.get(nodes[i]).insert(key, value);
        String[] keys = this.keys.clone();
        long[] nodes = this.nodes.clone();
        if (result.vals == null) {
            if (nodes[i+1] != 0) nodes[i+2] = nodes[i+1];
            nodes[i+1] = result.nodes[1];
            nodes[i] = result.nodes[0];
            if (keys[i] != null) keys[i+1] = keys[i];
            keys[i] = result.keys[0];
        } else {
            nodes[i] = storage.write(result);
        }
        return new BTree(storage, keys, nodes);
    }

    private BTree splitLeaf(String key, String value) {
        String[] leftKeys = this.keys.clone();
        String[] leftVals = this.vals.clone();
        String[] rightKeys = new String[this.keys.length];
        String[] rightVals = new String[this.keys.length];
        final int d = this.keys.length / 2;
        for (int i = 0; i < d; i++) {
            rightKeys[i] = leftKeys[i + d];
            rightVals[i] = leftVals[i + d];
            leftKeys[i + d] = null;
        }

        String[] keys = new String[d * 2];
        long[] nodes = new long[d * 2 + 1];
        BTree left = new BTree(storage, leftKeys, leftVals);
        BTree right = new BTree(storage, rightKeys, rightVals);
        nodes[0] = storage.write(left);
        nodes[1] = storage.write(right);
        keys[0] = right.keys[0];
        return new BTree(storage, keys, nodes).insert(key, value);
    }

    public BTree insert(String key) {
        return insert(key, null);
    }

    public BTree node(int i) {
        return storage.get(nodes[i]);
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < keys.length; i++) {
            if (nodes != null) {
                if (nodes[i] == 0) {
                    sb.append("[]");
                } else {
                    sb.append(nodes[i]);
                    sb.append(":");
                    sb.append(storage.get(nodes[i]).toString());
                }
            }
            sb.append(" ");
            if (keys[i] != null) {
                sb.append(keys[i]);
                if (vals != null) {
                    sb.append(vals[i] == null ? "." : "*");
                }
            } else {
                sb.append(".");
            }
            sb.append(" ");
        }
        sb.append("]");
        return sb.toString();
    }
}
