package net.avh4.data.per;

import net.avh4.data.per.service.RefService;
import net.avh4.data.per.service.TransactionException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RefRepositoryTest {
    private static final String refName = "__REF_NAME__";
    private static final String hash1 = "__HASH_1__";
    private static final String hash2 = "__HASH_2__";
    private static final String hash3 = "__HASH_3__";
    private static final String hash4 = "__HASH_4__";
    private RefRepository subject;
    @Mock private RefService service;
    @Mock private Transaction transaction;
    @Mock private Object storedContent1;
    @Mock private Object newContent2;
    @Mock private Object storedContent3;
    @Mock private Object newContent4;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        stub(service.getContentKey(refName)).toReturn(hash1);
        stub(service.getContent(hash1)).toReturn(storedContent1);
        stub(service.getContent(hash3)).toReturn(storedContent3);
        stub(service.put(newContent2)).toReturn(hash2);
        stub(service.put(newContent4)).toReturn(hash4);
        stub(transaction.transform(storedContent1)).toReturn(newContent2);
        stub(transaction.transform(storedContent3)).toReturn(newContent4);
        subject = new RefRepository(service);
    }

    @Test
    public void getContent_withPersistedData_shouldContainItems() throws Exception {
        assertThat(subject.getContent(refName)).isEqualTo(storedContent1);
    }

    @Test
    public void getContent_withNoPersistedData_shouldBeEmpty() throws Exception {
        stub(service.getContentKey(refName)).toReturn(null);
        assertThat(subject.getContent(refName)).isNull();
    }

    @Test(expected = RuntimeException.class)
    public void getContent_withCorruptedStore_shouldThrow() throws Exception {
        stub(service.getContent(hash1)).toReturn(null);
        subject.getContent(refName);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void execute_shouldPersistTheNewItem() throws Exception {
        subject.execute(refName, transaction);

        verify(transaction).transform(storedContent1);
        final InOrder inOrder = inOrder(service);
        inOrder.verify(service).put(newContent2);
        inOrder.verify(service).updateRef(refName, hash1, hash2);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void execute_whenRefIsChangedBeforeTransactionCompletes_shouldRetryTransaction() throws Exception {
        doAnswer(new Answer() {
            @Override public Object answer(InvocationOnMock invocation) throws Throwable {
                stub(service.getContentKey(refName)).toReturn(hash3);
                throw new TransactionException("");
            }
        }).when(service).updateRef(refName, hash1, hash2);

        subject.execute(refName, transaction);

        verify(transaction).transform(storedContent3);
        final InOrder inOrder = inOrder(service);
        inOrder.verify(service).put(newContent4);
        inOrder.verify(service).updateRef(refName, hash3, hash4);
    }
}
