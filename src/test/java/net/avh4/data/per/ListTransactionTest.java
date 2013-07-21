package net.avh4.data.per;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class ListTransactionTest {

    private ListTransaction<String> subject;

    @Before
    public void setUp() throws Exception {
        subject = new ListTransaction<String>() {
            @Override public void mutate(List<String> mutableList) {
                mutableList.add("Z");
            }
        };
    }

    @Test
    public void transform_shouldMutateList() throws Exception {
        assertThat(subject.transform(ImmutableList.of("A", "B"))).containsExactly("A", "B", "Z");
    }

    @Test
    public void transform_withNull_shouldProvideEmptyList() throws Exception {
        assertThat(subject.transform(null)).containsExactly("Z");
    }
}
