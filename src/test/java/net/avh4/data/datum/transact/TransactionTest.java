package net.avh4.data.datum.transact;

import net.avh4.data.datum.prim.Datum;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.Assertions.assertThat;

public class TransactionTest {
    private Transaction subject;
    @Mock private Datum datum;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subject = new Transaction();
    }

    @Test
    public void set_shouldReturnTransaction() throws Exception {
        assertThat(subject.set(datum)).isNotNull();
    }

    @Test
    public void assertions_shouldReturnDatums() throws Exception {
        subject = subject.set(datum);
        assertThat(subject.assertions()).containsOnly(datum);
    }

    @Test
    public void add_shouldReturnTransaction() throws Exception {
        assertThat(subject.add(datum)).isNotNull();
    }

    @Test
    public void additions_shouldReturnDatums() throws Exception {
        subject = subject.add(datum);
        assertThat(subject.additions()).containsOnly(datum);
    }
}
