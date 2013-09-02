package net.avh4.data.datum.peer.java;

import net.avh4.data.datum.prim.Id;
import net.avh4.data.datum.prim.KnownId;
import net.avh4.data.datum.store.DatumStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.stub;

public class DocumentInvocationHandlerTest {
    private TestDocument subject;
    private static final Id entity = new KnownId("__ENTITY_ID__");
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
        stub(store.get(new KnownId("__SUB_DOC_ID__"), "string")).toReturn("marine");
        assertThat(subject.subDocument().string()).isEqualTo("marine");
    }

    @Test
    public void method_withStringArrayReturnValue() throws Exception {
        stub(store.getArray(entity, "strings")).toReturn(new String[]{"A", "B", "C"});
        assertThat(subject.strings()).containsOnly("A", "B", "C");
    }

    @Test
    public void method_withDocumentArrayReturnValue() throws Exception {
        stub(store.getArray(entity, "subDocuments")).toReturn(new String[]{"ID_A", "ID_B"});
        stub(store.get(new KnownId("ID_A"), "string")).toReturn("Marina");
        stub(store.get(new KnownId("ID_B"), "string")).toReturn("Marimba");
        assertThat(subject.subDocuments()).hasSize(2).hasAllElementsOfType(TestDocument.SubDocument.class);
        assertThat(subject.subDocuments()[0].string()).isEqualTo("Marina");
        assertThat(subject.subDocuments()[1].string()).isEqualTo("Marimba");
    }

    @Test
    public void idMethod() throws Exception {
        assertThat(subject._id()).isEqualTo(entity);
    }

    private interface TestDocument {
        Id _id();

        String string();

        String[] strings();

        SubDocument subDocument();

        SubDocument[] subDocuments();

        interface SubDocument {
            String string();
        }
    }
}
