package util;

import java.util.Iterator;

public class DelegatingIterator<T> extends DelegatingIterator2<T, T> {

    public DelegatingIterator(Iterator<T> toDelegateTo) {
        super(toDelegateTo);
    }

    @Override
    public T next() {
        return getDelegate().next();
    }
}
