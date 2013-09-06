package net.avh4.data.datum.transact;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class InMemoryRefTest {
    private InMemoryRef<String> subject;

    @Before
    public void setUp() throws Exception {
        subject = new InMemoryRef<String>("In a galaxy far, far away");
    }

    @Test
    public void hasInitialValue() throws Exception {
        assertThat(subject.get()).isEqualTo("In a galaxy far, far away");
    }

    @Test
    public void commit_updatesValue() throws Exception {
        subject.commit("It is a period of civil war.");
        assertThat(subject.get()).isEqualTo("It is a period of civil war.");
    }

    @Test
    public void commit_returnsTheValue() throws Exception {
        final String result = subject.commit("It is a period of civil war.");
        assertThat(result).isEqualTo("It is a period of civil war.");
    }
}
