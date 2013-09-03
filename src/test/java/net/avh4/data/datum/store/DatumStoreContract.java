package net.avh4.data.datum.store;

import net.avh4.data.datum.prim.TempId;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public abstract class DatumStoreContract {
    private DatumStore subject;
    private String id1, id2, id3, id4;
    private TempId a;
    private TempId b;

    protected abstract DatumStore createSubject();

    @Before
    public void setUp() throws Exception {
        id1 = "1";
        id2 = "2";
        id3 = "3";
        id4 = "4";
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
        subject = subject.set(id1, "hasColor", "blue");
        assertThat(subject.get(id1, "hasColor")).isEqualTo("blue");
    }

    @Test
    public void write_shouldReplaceValues() throws Exception {
        subject = subject.set(id1, "hasColor", "blue");
        subject = subject.set(id1, "hasColor", "red");
        assertThat(subject.get(id1, "hasColor")).isEqualTo("red");
    }

    @Test
    public void write_shouldNotModifyOriginalStore() throws Exception {
        subject.set(id1, "hasColor", "blue");
        assertThat(subject.get(id1, "hasColor")).isNull();
    }

//    @Test
//    public void getArray_withNoValue_shouldReturnEmpty() throws Exception {
//        assertThat(subject.getArray(id1, "watches")).isEmpty();
//    }
//
//    @Test
//    public void getArray_shouldReturnStoreValues() throws Exception {
//        subject = subject.add(new ValueDatum(id1, "watches", "House M.D."));
//        assertThat(subject.getArray(id1, "watches")).containsOnly("House M.D.");
//    }

//    @Test
//    public void add_shouldAccumulateValues() throws Exception {
//        subject = subject.add(new ValueDatum(id1, "watches", "House M.D."));
//        subject = subject.add(new ValueDatum(id1, "watches", "Once Upon A Time"));
//        assertThat(subject.getArray(id1, "watches")).containsOnly("House M.D.", "Once Upon A Time");
//    }

//    @Test
//    public void add_shouldNotModifyOriginalStore() throws Exception {
//        subject.add(new ValueDatum(id1, "watches", "House M.D."));
//        assertThat(subject.getArray(id1, "watches")).isEmpty();
//    }

    @Test
    public void iterate_whenAllValuesMatch_shouldReturnMatchingValues() throws Exception {
        subject = subject.addIndex("name", "Aaron", id1);
        subject = subject.addIndex("name", "Betty", id2);

        assertThat(subject.iterate("name", "Aaron", "Betty")).containsOnly(id1, id2);
    }

    @Test
    public void iterate_shouldReturnMatchingValues() throws Exception {
        subject = subject.addIndex("name", "Aaron", id1);
        subject = subject.addIndex("name", "Betty", id2);
        subject = subject.addIndex("name", "Charlie", id3);
        subject = subject.addIndex("name", "Debra", id4);

        assertThat(subject.iterate("name", "Betty", "Charlie")).containsOnly(id2, id3);
    }

    @Test
    public void iterate_withRemovedValue_shouldReturnMatchingValues() throws Exception {
        subject = subject.addIndex("name", "Aaron", id1);
        subject = subject.addIndex("name", "Betty", id2);
        subject = subject.removeIndex("name", "Aaron", id1);

        assertThat(subject.iterate("name", "Aaron", "Betty")).containsOnly( id2);
    }


}
