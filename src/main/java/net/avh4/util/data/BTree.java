package net.avh4.util.data;

import java.util.Arrays;

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

    public BTree(BTreeStorage storage, String[] keys, String[] vals) {
        this.storage = storage;
        this.keys = keys;
        this.nodes = new long[keys.length + 1];
        this.vals = vals;
    }

    public BTree insert(String key, String value) {
        if (keys[keys.length - 1] != null) {
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
            leftKeys[d] = key;
            leftVals[d] = value;

            String[] keys = new String[d * 2];
            long[] nodes = new long[d * 2 + 1];
            keys[0] = key;
            nodes[0] = storage.write(new BTree(storage, leftKeys, leftVals));
            nodes[1] = storage.write(new BTree(storage, rightKeys, rightVals));
            return new BTree(storage, keys, nodes, null);
        } else {
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
            return new BTree(storage, keys, nodes, vals);
        }
    }

    public BTree insert(String key) {
        return insert(key, null);
    }

    public BTree node(int i) {
        return storage.get(nodes[i]);
    }

    @Override public String toString() {
        return Arrays.toString(keys);
    }
}
