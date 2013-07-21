package net.avh4.data.per;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.Serializable;

import static org.fest.assertions.Assertions.assertThat;

public class InMemoryRefServiceTest extends RefServiceContract<Serializable> {

    @Override protected RefService<Serializable> createSubject() {
        MockitoAnnotations.initMocks(this);
        return new InMemoryRefService();
    }

    @Override protected Serializable createObject(int i) {
        return "String-" + Integer.toString(i, 32);
    }

    @Test
    public void shouldSimulateSerializationOfObjects() throws Exception {
        final Serializable original = ImmutableList.of("A", "B", "X");
        final String key = subject.put(original);
        final Object retrieved = subject.getContent(key);
        assertThat(retrieved).isNotSameAs(original);
    }
}
