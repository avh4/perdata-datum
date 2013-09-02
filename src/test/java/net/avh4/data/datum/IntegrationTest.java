package net.avh4.data.datum;

import org.junit.Before;
import org.junit.Test;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;

import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class IntegrationTest {
    private Database db;
    private String bookId;

    public interface Book {
        public String title();

        public Person author();

        public Chapter[] chapters();

        public interface Person {
            public String name();
        }

        public interface Chapter {
            public String title();

            public String body();
        }
    }

    @Before
    public void setUp() throws Exception {
        MutablePicoContainer pico = new DefaultPicoContainer();
        pico.addComponent(DatabaseImpl.class);
        pico.addComponent(MemoryDatumStore.class);
        db = pico.getComponent(DatabaseImpl.class);
    }

    @Test
    public void testQuery() throws Exception {
        createTestData();

        Book book = db.get(Book.class, bookId);
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
    public void testGet() throws Exception {
        createTestData();

        Book[] books = db.query(Book.class);
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
        final List<String> ret = db.transact(new Transaction<List<String>>() {
            @Override public List<String> run(TransactionContext db) {

                String book = db.create();
                db.set(book, "title", "The Big Orange Splot");

                String author = db.create();
                db.set(author, "name", "Daniel Pinkwater");
                db.set(book, "author", author);

                String chapter1 = db.create();
                db.set(chapter1, "title", "Chapter 1");
                db.set(chapter1, "body", "Mr. Plumbean lived on a ...");
                db.add(book, "chapters", chapter1);

                return Arrays.asList(book, author);
            }
        });
        bookId = ret.get(0);
        final String author = ret.get(1);

        db.transact(new Transaction<Void>() {
            @Override public Void run(TransactionContext db) {
                db.set(author, "name", "Daniel Manus Pinkwater");

                String chapter2 = db.create();
                db.set(chapter2, "title", "Chapter 2");
                db.set(chapter2, "body", "He liked it that ...");
                db.add(bookId, "chapters", chapter2);

                return null;
            }
        });
    }
}
