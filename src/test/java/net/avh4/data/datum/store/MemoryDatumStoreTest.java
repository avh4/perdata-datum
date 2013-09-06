package net.avh4.data.datum.store;

import net.avh4.data.datum.primitives.TempId;
import org.junit.Before;
import org.junit.Test;

import static net.avh4.util.test.SerializationUtils.serialize;
import static org.fest.assertions.Assertions.assertThat;

public class MemoryDatumStoreTest extends DatumStoreContract {
    @Override protected DatumStore createSubject() {
        return new MemoryDatumStore();
    }

    private TempId id1, id2;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        id1 = new TempId();
        id2 = new TempId();
    }

    @Test
    public void shouldSerializeNextId() throws Exception {
        subject = subject.createId(id1).createId(id2);

        TempId expectedNextId = new TempId();
        subject.createId(expectedNextId);

        subject = serialize(subject);

        TempId actualNextId = new TempId();
        subject.createId(actualNextId);

        assertThat(actualNextId.id()).isEqualTo(expectedNextId.id());
    }

    @Test
    public void shouldSerializeTupleStore() throws Exception {
        subject = subject.set("A", "ay", "1").set("B", "bee", "2");

        assertThat(subject.get("A", "ay")).isEqualTo("1");
        assertThat(subject.get("B", "bee")).isEqualTo("2");

        subject = serialize(subject);

        assertThat(subject.get("A", "ay")).isEqualTo("1");
        assertThat(subject.get("B", "bee")).isEqualTo("2");
    }

    @Test
    public void shouldSerializeIndexes() throws Exception {
        subject = subject.addIndex("ay", "1", "A").addIndex("bee", "2", "B");

        assertThat(subject.iterate("ay", "1", "1")).containsOnly("A");
        assertThat(subject.iterate("bee", "2", "2")).containsOnly("B");

        subject = serialize(subject);

        assertThat(subject.iterate("ay", "1", "1")).containsOnly("A");
        assertThat(subject.iterate("bee", "2", "2")).containsOnly("B");
    }
}
