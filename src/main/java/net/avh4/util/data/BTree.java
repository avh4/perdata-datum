package net.avh4.util.data;

public class BTree {
    private final BTreeStorage storage;
    public final String[] keys;
    public final long[] nodes;
    public final String[] vals;

    public BTree(BTreeStorage storage, int d) {
        this.storage = storage;
        keys = new String[d * 2];
        nodes = new long[d * 2];
        vals = new String[d * 2];
    }

    public BTree(BTreeStorage storage, String[] keys, long[] nodes, String[] vals) {
        this.storage = storage;
        this.keys = keys;
        this.nodes = nodes;
        this.vals = vals;
    }

    public static net.avh4.util.data.BTree empty() {
        return null;
    }

    public BTree insert(String key, String value) {
        final String[] keys = this.keys.clone();
        final String[] vals = this.vals.clone();
        if (keys[0] == null) {
            keys[0] = key;
            vals[0] = value;
            return new BTree(storage, keys, nodes, vals);
        } else {
            if (key.compareTo(keys[0]) < 0) {
                for (int i = keys.length - 1; i > 0; i--) {
                    keys[i] = keys[i - 1];
                    vals[i] = vals[i - 1];
                }
                keys[0] = key;
                vals[0] = value;
                return new BTree(storage, keys, nodes, vals);
            } else {
                for (int i = keys.length - 1; i > 1; i--) {
                    keys[i] = keys[i - 1];
                    vals[i] = vals[i - 1];
                }
                keys[1] = key;
                vals[1] = value;
                return new BTree(storage, keys, nodes, vals);
            }
        }
    }

    public BTree node(int i) {
        return storage.get(nodes[i]);
    }
}
