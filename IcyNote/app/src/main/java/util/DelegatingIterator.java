package util;

import java.util.Iterator;

public class DelegatingIterator<T> implements Iterator<T>{
    Iterator<T> delegate;

    public DelegatingIterator(Iterator<T> toDelegateTo){
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
