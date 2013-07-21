package net.avh4.data.per;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

public class ListRefTest {
    private static final String refName = "__REF_NAME__";
    private static final Class<TestObject> clazz = TestObject.class;
    private ListRef<TestObject> subject;
    @Mock private RefRepository repository;
    private ImmutableList<TestObject> content;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        content = ImmutableList.of(mock(TestObject.class), mock(TestObject.class));
        subject = new ListRef<>(repository, refName, clazz);
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

    private static class TestObject {
    }
}
