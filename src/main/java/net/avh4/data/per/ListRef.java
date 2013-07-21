package net.avh4.data.per;

import com.google.common.collect.ImmutableList;

public class ListRef<T> extends Ref<ImmutableList<T>> {

    protected final Class<T> itemClass;

    public ListRef(RefRepository repository, String name, Class<T> itemClass) {
        super(repository, name);
        this.itemClass = itemClass;
    }

    public ImmutableList<T> content() {
        final Object content = repository.getContent(name);
        if (content == null) return ImmutableList.of();
        if (!(content instanceof ImmutableList))
            throw new ClassCastException("Content " + content + " is not an ImmutableList");
        for (Object o : (ImmutableList) content) {
            if (!itemClass.isInstance(o))
                throw new ClassCastException("Item " + o.toString() + " is not an instance of " + itemClass.toString());
        }
        //noinspection unchecked
        return (ImmutableList<T>) content;
    }

    public void execute(Transaction<ImmutableList<T>> transaction) {
        repository.execute(name, transaction);
    }
}
