package net.avh4.data.datum.transact;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.Assertions.assertThat;

public class TransactionTest {
    private Transaction subject;
    @Mock private Command c1;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subject = new Transaction();
    }

    @Test
    public void and_shouldReturnTransaction() throws Exception {
        assertThat(subject.and(c1)).isNotNull();
    }

    @Test
    public void commands_shouldReturnCommands() throws Exception {
        subject = subject.and(c1);
        assertThat(subject.commands()).containsOnly(c1);
    }
}
