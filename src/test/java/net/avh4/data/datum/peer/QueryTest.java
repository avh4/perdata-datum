package net.avh4.data.datum.peer;

import net.avh4.data.datum.prim.Id;
import net.avh4.data.datum.prim.KnownId;
import net.avh4.data.datum.store.DatumStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.stub;

public class QueryTest {
    private Query subject;
    @Mock private DatumStore store;
    private String matchingId = "__ID__";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        stub(store.iterate("action", "value", "value")).toReturn(Arrays.asList(matchingId));
        subject = new Query("action", "value");
    }

    @Test
    public void execute_shouldReturnMatchingResults() throws Exception {
        assertThat(subject.execute(store)).containsOnly(new KnownId(matchingId));
    }
}
