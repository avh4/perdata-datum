package net.avh4.data.datum.store;

import net.avh4.data.datum.prim.Id;
import net.avh4.data.datum.prim.KnownId;
import net.avh4.data.datum.prim.TempId;
import net.avh4.data.datum.prim.ValueDatum;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public abstract class DatumStoreContract {
    private DatumStore subject;
    private Id id1, id2, id3, id4;
    private TempId a;
    private TempId b;

    protected abstract DatumStore createSubject();

    @Before
    public void setUp() throws Exception {
        id1 = new KnownId("1");
        id2 = new KnownId("2");
        id3 = new KnownId("3");
        id4 = new KnownId("4");
        a = new TempId();
        b = new TempId();
        subject = createSubject();
    }

    @Test
    public void createId_shouldProvideIds() throws Exception {
        subject.createId(a);
        assertThat(a.id()).isNotNull();
    }

    @Test
    public void createId_shouldProvideUniqueValues() throws Exception {
        subject.createId(a).createId(b);
        assertThat(a.id()).isNotEqualTo(b.id());
    }

    @Test
    public void createIds_shouldReturnNewStore() throws Exception {
        subject = subject.createId(a);
        assertThat(subject).isNotNull();
    }

    @Test
    public void createIds_multipleTimes_shouldNotDuplicateIds() throws Exception {
        subject = subject.createId(a).createId(b);
        assertThat(a.id()).isNotEqualTo(b.id());
    }

    @Test
    public void get_withNoValue_shouldReturnNull() throws Exception {
        assertThat(subject.get(id1, "hasColor")).isNull();
    }

    @Test
    public void get_shouldReturnStoredValue() throws Exception {
        subject = subject.set(new ValueDatum(id1, "hasColor", "blue"));
        assertThat(subject.get(id1, "hasColor")).isEqualTo("blue");
    }

    @Test
    public void write_shouldReplaceValues() throws Exception {
        subject = subject.set(new ValueDatum(id1, "hasColor", "blue"));
        subject = subject.set(new ValueDatum(id1, "hasColor", "red"));
        assertThat(subject.get(id1, "hasColor")).isEqualTo("red");
    }

    @Test
    public void write_shouldNotModifyOriginalStore() throws Exception {
        subject.set(new ValueDatum(id1, "hasColor", "blue"));
        assertThat(subject.get(id1, "hasColor")).isNull();
    }

    @Test
    public void getArray_withNoValue_shouldReturnEmpty() throws Exception {
        assertThat(subject.getArray(id1, "watches")).isEmpty();
    }

    @Test
    public void getArray_shouldReturnStoreValues() throws Exception {
        subject = subject.add(new ValueDatum(id1, "watches", "House M.D."));
        assertThat(subject.getArray(id1, "watches")).containsOnly("House M.D.");
    }

    @Test
    public void add_shouldAccumulateValues() throws Exception {
        subject = subject.add(new ValueDatum(id1, "watches", "House M.D."));
        subject = subject.add(new ValueDatum(id1, "watches", "Once Upon A Time"));
        assertThat(subject.getArray(id1, "watches")).containsOnly("House M.D.", "Once Upon A Time");
    }

    @Test
    public void add_shouldNotModifyOriginalStore() throws Exception {
        subject.add(new ValueDatum(id1, "watches", "House M.D."));
        assertThat(subject.getArray(id1, "watches")).isEmpty();
    }

    @Test
    public void iterate_whenAllValuesMatch_shouldReturnMatchingValues() throws Exception {
        subject = subject.set(new ValueDatum(id1, "name", "Aaron"));
        subject = subject.set(new ValueDatum(id2, "name", "Betty"));

        assertThat(subject.iterate("name", "Aaron", "Betty")).containsOnly(id1, id2);
    }

    @Test
    public void iterate_shouldReturnMatchingValues() throws Exception {
        subject = subject.set(new ValueDatum(id1, "name", "Aaron"));
        subject = subject.set(new ValueDatum(id2, "name", "Betty"));
        subject = subject.set(new ValueDatum(id3, "name", "Charlie"));
        subject = subject.set(new ValueDatum(id4, "name", "Debra"));

        assertThat(subject.iterate("name", "Betty", "Charlie")).containsOnly(id2, id3);
    }
}
