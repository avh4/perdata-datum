package net.avh4.data.per2;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class DatabaseTest {
    private Database subject;

    @Mock private DatumStore store;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subject = new Database(store);
    }

    @Test
    public void create_shouldGenerateId() throws Exception {
        assertThat(subject.create()).isNotNull();
    }

    @Test
    public void create_shouldGenerateUniqueId() throws Exception {
        final EntityId entity1 = subject.create();
        final EntityId entity2 = subject.create();
        assertThat(entity1).isEqualTo(entity1);
        assertThat(entity2).isEqualTo(entity2);
        assertThat(entity1).isNotEqualTo(entity2);
        assertThat(entity2).isNotEqualTo(entity1);
    }

    @Test
    public void set_shouldStoreDatum() throws Exception {
        final EntityId entity = subject.create();
        subject.set(entity, "action", "value");
        verify(store).write(entity, "action", "value");
    }
}
