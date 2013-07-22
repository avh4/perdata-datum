package net.avh4.data.per;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ListRefTest {
    private static final String refName = "__REF_NAME__";
    private static final Class<TestObject> clazz = TestObject.class;
    private ListRef<TestObject> subject;
    private ImmutableList<TestObject> content;
    @Mock private RefRepository repository;
    @Mock private Transaction<ImmutableList<TestObject>> transaction;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        content = ImmutableList.of(mock(TestObject.class), mock(TestObject.class));
        subject = new ListRef<TestObject>(repository, refName, clazz);
    }

    @Test
    public void getContent_withPersistedData_shouldReturnTheContent() throws Exception {
        stub(repository.getContent(refName)).toReturn(content);
        assertThat(subject.content()).isEqualTo(content);
    }

    @Test
    public void getContent_withNoPersistedData_shouldReturnEmptyList() throws Exception {
        stub(repository.getContent(refName)).toReturn(null);
        assertThat(subject.content()).isEmpty();
    }

    @Test
    public void execute_shouldProxyTheRepository() throws Exception {
        subject.execute(transaction);
        verify(repository).execute(refName, transaction);
    }

    private static class TestObject {
    }
}
