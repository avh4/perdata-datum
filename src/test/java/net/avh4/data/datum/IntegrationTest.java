package net.avh4.data.datum;

import net.avh4.data.datum.peer.java.DatabaseImpl;
import net.avh4.data.datum.peer.java.DirectAccess;
import net.avh4.data.datum.peer.java.JavaQuery;
import net.avh4.data.datum.prim.*;
import net.avh4.data.datum.store.DatumStore;
import net.avh4.data.datum.store.MemoryDatumStore;
import net.avh4.data.datum.transact.LocalTransactor;
import net.avh4.data.datum.transact.Transaction;
import net.avh4.data.datum.transact.Transactor;
import org.junit.Before;
import org.junit.Test;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;

import static org.fest.assertions.Assertions.assertThat;

public class IntegrationTest {
    private Transactor transactor;
    private TempId bookId;
    private DatumStore store;
    private DirectAccess direct;

    public interface Book {
        String title();

        Person author();

        Chapter[] chapters();

        public interface Person {
            String name();
        }

        public interface Chapter {
            String title();

            String body();
        }
    }

    public interface Metadata {
        String attempts();
    }

    @Before
    public void setUp() throws Exception {
        MutablePicoContainer pico = new DefaultPicoContainer();
        pico.addComponent(DatabaseImpl.class);
        pico.addComponent(LocalTransactor.class);
        pico.addComponent(MemoryDatumStore.class);
        store = pico.getComponent(DatumStore.class);
        transactor = pico.getComponent(Transactor.class);
        direct = pico.getComponent(DirectAccess.class);
    }

    @Test
    public void testGet() throws Exception {
        createTestData();

        Metadata meta = direct.get(store, Metadata.class, new KnownId("integration:metadata"));
        assertThat(meta.attempts()).isEqualTo("2");

        Book book = direct.get(store, Book.class, bookId);
        assertThat(book).isNotNull();

        assertThat(book.title()).isNotNull().isEqualTo("The Big Orange Splot");
        assertThat(book.author()).isNotNull();
        assertThat(book.author().name()).isEqualTo("Daniel Manus Pinkwater");
        assertThat(book.chapters()).isNotNull().hasSize(2).hasAllElementsOfType(Book.Chapter.class);
        assertThat(book.chapters()[0].title()).isEqualTo("Chapter 1");
        assertThat(book.chapters()[0].body()).startsWith("Mr. Plumbean lived on a");
        assertThat(book.chapters()[1].title()).isEqualTo("Chapter 2");
        assertThat(book.chapters()[1].body()).startsWith("He liked it that");
    }

    @Test
    public void testQuery() throws Exception {
        createTestData();

        JavaQuery<Book> q = new JavaQuery<Book>(Book.class, "db:type", "book");
        Book[] books = q.execute(store);
        assertThat(books).isNotNull().hasSize(1).hasAllElementsOfType(Book.class);

        assertThat(books[0].title()).isNotNull().isEqualTo("The Big Orange Splot");
        assertThat(books[0].author()).isNotNull();
        assertThat(books[0].author().name()).isEqualTo("Daniel Manus Pinkwater");
        assertThat(books[0].chapters()).isNotNull().hasSize(2).hasAllElementsOfType(Book.Chapter.class);
        assertThat(books[0].chapters()[0].title()).isEqualTo("Chapter 1");
        assertThat(books[0].chapters()[0].body()).startsWith("Mr. Plumbean lived on a");
        assertThat(books[0].chapters()[1].title()).isEqualTo("Chapter 2");
        assertThat(books[0].chapters()[1].body()).startsWith("He liked it that");
    }

    private void createTestData() {
        final Id meta = new KnownId("integration:metadata");
        final TempId author = new TempId();
        {
            bookId = new TempId();
            final TempId chapter1 = new TempId();
            Transaction t1 = new Transaction()
                    .set(new ValueDatum(meta, "attempts", "1"))

                    .set(new ValueDatum(bookId, "db:type", "book"))
                    .set(new ValueDatum(bookId, "title", "The Big Orange Splot"))

                    .set(new ValueDatum(author, "name", "Daniel Pinkwater"))
                    .set(new RefDatum(bookId, "author", author))

                    .set(new ValueDatum(chapter1, "title", "Chapter 1"))
                    .set(new ValueDatum(chapter1, "body", "Mr. Plumbean lived on a ..."))
                    .add(new RefDatum(bookId, "chapters", chapter1));

            store = transactor.transact(t1);
        }

        {
            final TempId chapter2 = new TempId();
            Transaction t2 = new Transaction()
                    .set(new ValueDatum(meta, "attempts", "2"))

                    .set(new ValueDatum(author, "name", "Daniel Manus Pinkwater"))

                    .set(new ValueDatum(chapter2, "title", "Chapter 2"))
                    .set(new ValueDatum(chapter2, "body", "He liked it that ..."))
                    .add(new RefDatum(bookId, "chapters", chapter2));

            store = transactor.transact(t2);
        }
    }
}
