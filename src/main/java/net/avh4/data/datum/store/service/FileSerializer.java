package net.avh4.data.datum.store.service;

import java.io.*;

class FileSerializer<T extends Serializable> implements Serializer<T> {
    private final File root;

    public FileSerializer(File root) {
        this.root = root;
    }

    @Override public void writeToDisk(T value) throws IOException {
        final File file = new File(root, "FileSerializer.data");
        final FileOutputStream fos = new FileOutputStream(file);
        final ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(value);
    }
}
