package net.avh4.data.per2;

import java.util.ArrayList;

import static net.avh4.data.per2.DatumIntegrationTest.Book;

public class Database {

    private final DatumStore store;
    private int nextId = 0;
    private ArrayList<EntityId> ids = new ArrayList<EntityId>();

    public Database(DatumStore store) {
        this.store = store;
    }

    public EntityId create() {
        final EntityId entityId = new EntityId("" + (++nextId));
        ids.add(entityId);
        return entityId;
    }

    public void set(EntityId entityId, String action, Object value) {
        store.write(entityId, action, value.toString());
    }

    public void add(EntityId entityId, String action, Object value) {

    }

    public void transaction(Runnable runnable) {
        runnable.run();
    }

    public Book[] query(Class<Book> documentClass) {
        final Book[] results = new Book[1];
        results[0] = new Book() {
            @Override public String title() {
                return store.get(ids.get(0), "title");
            }

            @Override public Person author() {
                return new Person() {
                    @Override public String name() {
                        return store.get(ids.get(1), "name");
                    }
                };
            }

            @Override public Chapter[] chapters() {
                return new Chapter[]{
                        new MyChapter(ids.get(2)),
                        new MyChapter(ids.get(3)),
                };
            }
        };
        return results;
    }

    private class MyChapter implements Book.Chapter {
        private final EntityId entityId;

        public MyChapter(EntityId entityId) {
            this.entityId = entityId;
        }

        @Override public String title() {
            return store.get(entityId, "title");
        }

        @Override public String body() {
            return store.get(entityId, "body");
        }
    }
}
