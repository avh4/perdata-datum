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
            if (key.compareTo(keys[0]) >= 0) {
                BTree right = storage.get(nodes[1]).insert(key, value);
                nodes[1] = storage.write(right);
                return new BTree(storage, keys, nodes);
            } else {
                BTree left = storage.get(nodes[0]).insert(key, value);
                nodes[0] = storage.write(left);
                return new BTree(storage, keys, nodes);
            }
        } else if (keys[keys.length - 1] != null) {
            return split(key, value);
        } else {
            return insertHere(key, value);
        }
    }

    private BTree insertHere(String key, String value) {
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

    private BTree split(String key, String value) {
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
        if (key.compareTo(rightKeys[0]) >= 0) {
            right = right.insert(key, value);
        } else {
            left = left.insert(key, value);
        }
        nodes[0] = storage.write(left);
        nodes[1] = storage.write(right);
        keys[0] = right.keys[0];
        return new BTree(storage, keys, nodes);
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
