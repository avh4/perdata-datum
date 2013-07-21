package net.avh4.data.per.hash;

import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;

import static org.fest.assertions.Assertions.assertThat;

public class MessageDigestHasherTest {

    private MessageDigestHasher<Serializable> sha1;

    @Before
    public void setUp() throws Exception {
        sha1 = MessageDigestHasher.getSha1();
    }

    @Test
    public void shouldHashStrings() throws Exception {
        assertThat(sha1.hash("String")).isEqualTo("217f1da79f54712c5aeb6f437b195da011c3ee0b");
        assertThat(sha1.hash("")).isEqualTo("67803351dc43bd7e282bb6ab5a1f6e6611bf8ba0");
    }

    @Test
    public void shouldHashSerializableObjects() throws Exception {
        assertThat(sha1.hash(new TestSerializableObject(7))).isEqualTo("b7437e58f097e4f4e5b9a19ef2d47f159476a2c8");
        assertThat(sha1.hash(new TestSerializableObject(0))).isEqualTo("6fbc7fac99f4cca5e35dd3c36bdc04621378ad31");
    }

    @Test
    public void shouldHashNull() throws Exception {
        assertThat(sha1.hash(null)).isEqualTo("c815f34691be353caa9de93bbdb00a31f62a9ed3");
    }

    private static class TestSerializableObject implements Serializable {
        private static final long serialVersionUID = 1L;

        public final int number;

        private TestSerializableObject(int number) {
            this.number = number;
        }
    }
}
