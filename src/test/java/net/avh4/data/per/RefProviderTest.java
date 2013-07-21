package net.avh4.data.per;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.stub;

public class RefProviderTest {
    private static final String refName = "__FAKE_REF_NAME__";
    private static final String hash = "__FAKE_HASH__";
    private RefProvider subject;
    @Mock private RefService service;
    @Mock private ImmutableList items;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subject = new RefProvider(service);
    }

    @Test
    public void newRef_withNoPersistedData_shouldBeEmpty() throws Exception {
        Ref ref = subject.getRef(refName);
        assertThat(ref.items()).isEmpty();
    }

    @Test
    public void newRef_withPersistedData_shouldContainItems() throws Exception {
        stub(service.getContentKey(refName)).toReturn(hash);
        stub(service.getItems(hash)).toReturn(items);
        Ref ref = subject.getRef(refName);
        assertThat(ref.items()).isEqualTo(items);
    }
}
