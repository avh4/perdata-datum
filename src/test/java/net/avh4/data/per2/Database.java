package net.avh4.data.per2;

import static net.avh4.data.per2.DatumIntegrationTest.Book;

public class Database {
    public EntityId create() {
        return null;
    }

    public void set(EntityId entityId, String action, Object value) {

    }

    public void add(EntityId entityId, String action, Object value) {

    }

    public void transaction(Runnable runnable) {

    }

    public Book[] query(Class<Book> documentClass) {
        final Book[] results = new Book[1];
        results[0] = new Book() {
            @Override public String title() {
                return "The Big Orange Splot";
            }

            @Override public Person[] authors() {
                return new Person[]{
                        new Person() {
                            @Override public String name() {
                                return "Daniel Manus Pinkwater";
                            }
                        }
                };
            }

            @Override public Chapter[] chapters() {
                return new Chapter[]{
                        new Chapter() {
                            @Override public String title() {
                                return "Chapter 1";
                            }

                            @Override public String body() {
                                return "Mr. Plumbean lived on a";
                            }
                        },
                        new Chapter() {
                            @Override public String title() {
                                return "Chapter 2";
                            }

                            @Override public String body() {
                                return "He liked it that";
                            }
                        }
                };
            }
        };
        return results;
    }
}
