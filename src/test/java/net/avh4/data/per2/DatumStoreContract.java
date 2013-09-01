package net.avh4.data.per2;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public abstract class DatumStoreContract {
    private DatumStore subject;
    private String id1;

    protected abstract DatumStore createSubject();

    @Before
    public void setUp() throws Exception {
        subject = createSubject();
        id1 = "1";
    }

    @Test
    public void get_withNoValue_shouldReturnNull() throws Exception {
        assertThat(subject.get(id1, "hasColor")).isNull();
    }

    @Test
    public void get_shouldReturnStoredValue() throws Exception {
        subject.write(id1, "hasColor", "blue");
        assertThat(subject.get(id1, "hasColor")).isEqualTo("blue");
    }

    @Test
    public void get_shouldReturnMostRecentValue() throws Exception {
        subject.write(id1, "hasColor", "blue");
        subject.write(id1, "hasColor", "red");
        assertThat(subject.get(id1, "hasColor")).isEqualTo("red");
    }
}
