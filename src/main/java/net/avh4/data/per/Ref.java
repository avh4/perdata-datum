package net.avh4.data.per;

import com.google.common.collect.ImmutableList;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class Ref<T> {

    private final RefProvider provider;
    private String contentKey;
    private ImmutableList<T> content;
    private String name;

    protected Ref(RefProvider provider, String name, String contentKey, ImmutableList<T> content) {
        this.provider = provider;
        this.name = name;
        this.contentKey = contentKey;
        this.content = content;
    }

    public List<T> items() {
        return content;
    }

    protected void setContent(String contentKey, ImmutableList<T> content) {
        checkNotNull(contentKey);
        checkNotNull(content);
        this.contentKey = contentKey;
        this.content = content;
    }

    public void execute(Transaction<? super List<T>> transaction) {
        provider.execute(this, transaction);
    }

    public String getName() {
        return name;
    }

    public String getContentKey() {
        return contentKey;
    }
}
