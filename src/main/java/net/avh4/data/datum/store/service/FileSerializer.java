package net.avh4.data.datum.store.service;

import java.io.*;

public class FileSerializer<T extends Serializable> implements Serializer<T> {
    private final File root;
    private T initialValue;

    public FileSerializer(File root, T initialValue) {
        this.root = root;
        this.initialValue = initialValue;
    }

    @Override public void writeToDisk(T value) throws IOException {
        final File file = new File(root, "FileSerializer.data");
        final FileOutputStream fos = new FileOutputStream(file);
        final ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(value);
    }

    @Override public T readLatest() throws IOException {
        final File file = new File(root, "FileSerializer.data");
        try {
            final FileInputStream fos = new FileInputStream(file);
            final ObjectInputStream oos = new ObjectInputStream(fos);
            return (T) oos.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Couldn't deserialize " + file, e);
        } catch (FileNotFoundException e) {
            return initialValue;
        }
    }
}
