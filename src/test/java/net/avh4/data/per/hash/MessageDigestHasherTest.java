package net.avh4.data.per.hash;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class MessageDigestHasherTest {
    @Test
    public void shouldHashStrings() throws Exception {
        assertThat(MessageDigestHasher.getSha1().hash("String")).isEqualTo("3df63b7acb0522da685dad5fe84b81fdd7b25264");
        assertThat(MessageDigestHasher.getSha1().hash("")).isEqualTo("da39a3ee5e6b4b0d3255bfef95601890afd80709");
    }
}
