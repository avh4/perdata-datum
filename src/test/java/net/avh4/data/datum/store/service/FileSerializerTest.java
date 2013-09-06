package net.avh4.data.datum.store.service;

import net.avh4.util.sandbox.Sandbox;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

import static org.fest.assertions.Assertions.assertThat;

public class FileSerializerTest {
    FileSerializer<String> subject;
    private Sandbox sandbox;

    @Before
    public void setUp() throws Exception {
        sandbox = new Sandbox();
        subject = new FileSerializer<String>(sandbox.getRoot());
    }

    @Test
    public void shouldSerializeObjectToDisk() throws Exception {
        subject.writeToDisk("It's been a long road, getting from there to here.");

        File file = new File(sandbox.getRoot(), "FileSerializer.data");
        final Object o = SerializationUtils.deserialize(new FileInputStream(file));
        assertThat((String) o).isEqualTo("It's been a long road, getting from there to here.");
    }
}
