package net.avh4.data.datum;

import net.avh4.data.datum.*;
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
    private String entity;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subject = new DatabaseImpl(store);
        entity = create();
    }

    @Test
    public void create_shouldGenerateId() throws Exception {
        assertThat(entity).isNotNull();
    }

    @Test
    public void create_shouldGenerateUniqueId() throws Exception {
        final String entity1 = create();
        final String entity2 = create();
        assertThat(entity1).isEqualTo(entity1);
        assertThat(entity2).isEqualTo(entity2);
        assertThat(entity1).isNotEqualTo(entity2);
        assertThat(entity2).isNotEqualTo(entity1);
    }

    @Test
    public void set_shouldStoreDatum() throws Exception {
        set(entity, "action", "value");
        verify(store).write(entity, "action", "value");
    }

    @Test
    public void set_withEntityId_shouldStoreDatum() throws Exception {
        set(entity, "self_reference", entity);
        verify(store).write(entity, "self_reference", entity);
    }

    @Test
    public void add_shouldStoreDatum() throws Exception {
        add(entity, "links", "value1");
        verify(store).write(entity, "links", "[\"value1\"]");
    }

    @Test
    public void add_withExistingValue_shouldAddValue() throws Exception {
        stub(store.get(entity, "links")).toReturn("[\"value1\"]");

        add(entity, "links", "value2");
        verify(store).write(entity, "links", "[\"value1\",\"value2\"]");
    }

    @Test
    public void add_withEntityId_shouldStoreDatum() throws Exception {
        add(entity, "self_reference", entity);
        verify(store).write(entity, "self_reference", "[\"" + entity + "\"]");
    }

    private String create() {
        return subject.transact(new Transaction<String>() {
            @Override public String run(TransactionContext t) {
                return t.create();
            }
        });
    }

    private void set(final String entity, final String action, final String value) {
        subject.transact(new Transaction<Void>() {
            @Override public Void run(TransactionContext t) {
                return t.set(entity, action, value);
            }
        });
    }

    private void add(final String entity, final String action, final String value) {
        subject.transact(new Transaction<Void>() {
            @Override public Void run(TransactionContext t) {
                return t.add(entity, action, value);
            }
        });
    }
}
