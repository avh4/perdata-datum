package net.avh4.data.per;

import net.avh4.data.per.service.RefService;
import net.avh4.data.per.service.TransactionException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Random;

import static org.fest.assertions.Assertions.assertThat;

public abstract class RefServiceContract<T> {

    protected RefService<T> subject;
    private String key1;
    private String key2;

    protected abstract RefService<T> createSubject();

    protected abstract T createObject(int i);

    @Before
    public void setUp() throws Exception {
        subject = createSubject();
        T o1 = createObject(1);
        T o2 = createObject(2);
        key1 = subject.put(o1);
        key2 = subject.put(o2);
    }

    @Test
    public void getContentKey_forUnknownRef_shouldReturnNull() throws Exception {
        assertThat(subject.getContentKey("unknown-ref")).isNull();
    }

    @Test(expected = RuntimeException.class)
    public void updateRef_withUnknownContentKey_shouldThrow() throws Exception {
        subject.updateRef("new-ref", null, "bad-key");
    }

    @Test
    public void updateRef_withValueContentKey_shouldUpdateRef() throws Exception {
        subject.updateRef("new-ref", null, key1);
        assertThat(subject.getContentKey("new-ref")).isEqualTo(key1);
    }

    @Test
    public void updateRef_withNull_shouldRemoveRef() throws Exception {
        subject.updateRef("new-ref", null, key1);
        subject.updateRef("new-ref", key1, null);
        assertThat(subject.getContentKey("new-ref")).isNull();
    }

    @Test(expected = TransactionException.class)
    public void updateRef_whenNewRefIsThoughtToExist_shouldThrow() throws Exception {
        subject.updateRef("new-ref", key2, key1);
    }

    @Test(expected = TransactionException.class)
    public void updateRef_whenNewRefIsThoughtNotToExist_shouldThrow() throws Exception {
        subject.updateRef("new-ref", null, key1);
        subject.updateRef("new-ref", null, key2);
    }

    @Test
    public void put_withString_shouldStoreTheContent() throws Exception {
        final T object = createObject(3);
        final String key = subject.put(object);
        assertThat(subject.getContent(key)).isEqualTo(object);
    }

    @Test
    public void put_withTheSameContent_shouldHaveTheSameKey() throws Exception {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            final int randI = new BigInteger(130, random).intValue();
            final T content = createObject(randI);
            final String key1 = subject.put(content);
            final String key2 = subject.put(content);
            assertThat(key1).isEqualTo(key2);
        }
    }

    @Test
    public void put_withDifferentContent_shouldHaveDifferentKeys() throws Exception {
        HashSet<T> values = new HashSet<T>(100);
        HashSet<String> keys = new HashSet<String>(100);
        for (int i = 0; i < 100; i++) {
            Random random = new Random();
            T content;
            do {
                final int randI = new BigInteger(130, random).intValue();
                content = createObject(randI);
            } while (values.contains(content));
            String key = subject.put(content);
            assertThat(key).isNotIn(keys);
            values.add(content);
            keys.add(key);
        }
    }
}
