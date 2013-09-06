package net.avh4.data.datum.store.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class SynchronouslySerializedRefTest extends RefContract {
    @Mock private Serializer<String> serializer;

    @Before @Override
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setUp();
    }

    @Override protected Ref<String> createSubject(String initialValue) {
        return new SynchronouslySerializedRef<String>(serializer, initialValue);
    }

    @Test
    public void commit_shouldSerializeToDisk() throws Exception {
        subject.commit("It is a dark time for the Rebellion.");

        verify(serializer).writeToDisk("It is a dark time for the Rebellion.");
    }
}
