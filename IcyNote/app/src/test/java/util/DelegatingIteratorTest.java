package util;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;


public abstract class DelegatingIteratorTest<T> {

    private DelegatingIterator<T> toTest = null;

    abstract DelegatingIterator<T> makeNewEmpty();

    abstract DelegatingIterator<T> makeNewNonEmpty();

    abstract T firstElement();

    abstract T secondElement();


    @Before
    public void setUp() {
        toTest = makeNewNonEmpty();
    }

    @Test
    public void hasNextWhenEmpty() {
        toTest = makeNewEmpty();
        assertFalse(toTest.hasNext());
    }

    @Test
    public void hasNextWhenNonEmpty() {
        assertTrue(toTest.hasNext());
    }

    @Test
    public void next() {
        assertSame(firstElement(), toTest.next());
        assertSame(secondElement(), toTest.next());
    }

    @Test
    public void remove() {
        assertSame(firstElement(), toTest.next());
        toTest.remove();
        assertSame(firstElement(), toTest.next());
    }

}