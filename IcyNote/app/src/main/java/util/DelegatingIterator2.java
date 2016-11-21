package util;

import java.util.Iterator;

public abstract class DelegatingIterator2<S, T> implements Iterator<T> {
    private Iterator<S> delegate;

    public DelegatingIterator2(Iterator<S> toDelegateTo) {
        if (toDelegateTo == null) {
            //do not use the core.Checker class here
            throw new IllegalArgumentException("cannot create DelegatingIterator with null delegate");
        }

        delegate = toDelegateTo;
    }

    @Override
    public boolean hasNext() {
        return delegate.hasNext();
    }

    @Override
    public void remove() {
        delegate.remove();
    }

    protected Iterator<S> getDelegate() {
        return delegate;
    }
}
