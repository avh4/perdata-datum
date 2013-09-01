package net.avh4.data.per2;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.stub;

public class DocumentInvocationHandlerTest {
    private TestDocument subject;
    @Mock private EntityId entity;
    @Mock private DatumStore store;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subject = DocumentInvocationHandler.getDocument(store, TestDocument.class, entity);
    }

    @Test
    public void method_withStringReturnValue() throws Exception {
        stub(store.get(entity, "string")).toReturn("beans");
        assertThat(subject.string()).isEqualTo("beans");
    }

    private interface TestDocument {
        String string();
    }
}
