package net.avh4.util.test;

import java.io.*;

import static org.fest.assertions.Assertions.assertThat;

public class SerializationUtils {
    public static <T> T serialize(T subject) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(subject);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        final T store = (T) ois.readObject();
        assertThat(store).as("deserialized object was null").isNotNull();
        return store;
    }
}
