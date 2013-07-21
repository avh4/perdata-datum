package net.avh4.data.per;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.stub;

public class RefProviderTest {
    private static final String refName = "__FAKE_REF_NAME__";
    private static final String hash = "__FAKE_HASH__";
    private static final String newHash = "__FAKE_NEW_HASH__";
    private static final String emptyListHash = "__EMPTY_LIST_HASH__";
    private RefProvider subject;
    @Mock private RefService service;
    @Mock private ImmutableList<Cow> cows;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subject = new RefProvider(service);
        stub(service.put(Mockito.any(ImmutableList.class))).toReturn(newHash);
        stub(service.getEmptyListKey(Cow.class)).toReturn(emptyListHash);
        stub(service.getEmptyList(Cow.class)).toReturn(ImmutableList.<Cow>of());
    }

    @Test
    public void newRef_withNoPersistedData_shouldBeEmpty() throws Exception {
        Ref<Cow> ref = subject.getRef(refName, Cow.class);
        assertThat(ref.items()).isEmpty();
    }

    @Test
    public void newRef_withPersistedData_shouldContainItems() throws Exception {
        stub(service.getContentKey(refName)).toReturn(hash);
        stub(service.getItems(hash, Cow.class)).toReturn(cows);
        Ref ref = subject.getRef(refName, Cow.class);
        assertThat(ref.items()).isEqualTo(cows);
    }

    @Test
    public void add_shouldAddTheNewItem() throws Exception {
        Ref<Cow> ref = subject.getRef(refName, Cow.class);
        final Cow bessie = new Cow("Bessie", 14);
        ref.execute(new Transaction<List<Cow>>() {
            public void run(List<Cow> mutableItems) {
                mutableItems.add(bessie);
            }
        });
        assertThat(ref.items()).containsExactly(bessie);
    }

    @Test
    public void add_shouldPersistTheNewItem() throws Exception {
        Ref<Cow> ref = subject.getRef(refName, Cow.class);
        final Cow bessie = new Cow("Bessie", 14);
        ref.execute(new Transaction<List<Cow>>() {
            public void run(List<Cow> mutableItems) {
                mutableItems.add(bessie);
            }
        });
        final InOrder inOrder = inOrder(service);
        inOrder.verify(service).put(ImmutableList.of(bessie));
        inOrder.verify(service).updateRef(refName, emptyListHash, newHash);
    }

    private static class Cow {
        public Cow(String name, int age) {
        }
    }
}
