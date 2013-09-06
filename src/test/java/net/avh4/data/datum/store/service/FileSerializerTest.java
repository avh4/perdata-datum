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
        subject = new FileSerializer<String>(sandbox.getRoot(), null);
    }

    @Test
    public void shouldSerializeObjectToDisk() throws Exception {
        subject.writeToDisk("It's been a long road, getting from there to here.");

        File file = new File(sandbox.getRoot(), "FileSerializer.data");
        final Object o = SerializationUtils.deserialize(new FileInputStream(file));
        assertThat((String) o).isEqualTo("It's been a long road, getting from there to here.");
    }

    @Test
    public void readLatest_shouldReturnLastWrittenValue() throws Exception {
        subject.writeToDisk("Cause I've got faith of the heart.");

        subject = new FileSerializer<String>(sandbox.getRoot(), null);
        assertThat(subject.readLatest()).isEqualTo("Cause I've got faith of the heart.");
    }

    @Test
    public void withNoWrittenValue_readLatest_shouldReturnInitialValue() throws Exception {
        Sandbox sandbox2 = new Sandbox();
        subject = new FileSerializer<String>(sandbox2.getRoot(), "Check the circuit.");

        assertThat(subject.readLatest()).isEqualTo("Check the circuit.");
    }
}
