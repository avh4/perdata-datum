package net.avh4.data.per2;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

public class DatabaseTest {
    private Database subject;

    @Mock private DatumStore store;
    private EntityId entity;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subject = new Database(store);
        entity = subject.create();
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
        subject.set(entity, "action", "value");
        verify(store).write(entity, "action", "value");
    }

    @Test
    public void add_shouldStoreDatum() throws Exception {
        subject.add(entity, "links", "value1");
        verify(store).write(entity, "links", "[\"value1\"]");
    }

    @Test
    public void add_withExistingValue_shouldAddValue() throws Exception {
        stub(store.get(entity, "links")).toReturn("[\"value1\"]");

        subject.add(entity, "links", "value2");
        verify(store).write(entity, "links", "[\"value1\",\"value2\"]");
    }
}
