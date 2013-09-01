package net.avh4.data.per2;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.stub;

public class DocumentInvocationHandlerTest {
    private TestDocument subject;
    private String entity = "__ENTITY_ID__";
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

    @Test
    public void method_withDocumentReturnValue() throws Exception {
        stub(store.get(entity, "subDocument")).toReturn("__SUB_DOC_ID__");
        stub(store.get("__SUB_DOC_ID__", "string")).toReturn("marine");
        assertThat(subject.subDocument().string()).isEqualTo("marine");
    }

    @Test
    public void method_withStringArrayReturnValue() throws Exception {
        stub(store.get(entity, "strings")).toReturn("[\"A\",\"B\",\"C\"]");
        assertThat(subject.strings()).containsOnly("A", "B", "C");
    }

    private interface TestDocument {
        String string();
        String[] strings();
        SubDocument subDocument();

        interface SubDocument {
            String string();
        }
    }
}
