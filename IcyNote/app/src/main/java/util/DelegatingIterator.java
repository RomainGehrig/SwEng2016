package util;

import java.util.Iterator;

public class DelegatingIterator<T> implements Iterator<T> {
    private Iterator<T> delegate;

    public DelegatingIterator(Iterator<T> toDelegateTo) {
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
    public T next() {
        return delegate.next();
    }

    @Override
    public void remove() {
        delegate.remove();
    }
}
