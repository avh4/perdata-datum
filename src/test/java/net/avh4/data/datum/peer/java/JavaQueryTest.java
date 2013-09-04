package net.avh4.data.datum.peer.java;

import net.avh4.data.datum.peer.Query;
import net.avh4.data.datum.primitives.KnownId;
import net.avh4.data.datum.store.DatumStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.stub;

public class JavaQueryTest {

    private JavaQuery<TestObject> subject;
    @Mock private Query query;
    @Mock private DatumStore store;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        stub(query.execute(store)).toReturn(new KnownId[]{new KnownId("A"), new KnownId("B")});
        subject = new JavaQuery<TestObject>(TestObject.class, query);
    }

    @Test
    public void execute() throws Exception {
        final TestObject[] results = subject.execute(store);
        assertThat(results).hasSize(2).hasAllElementsOfType(TestObject.class);
    }

    private interface TestObject {
    }
}
