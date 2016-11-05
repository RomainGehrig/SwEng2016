package util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertNotNull;

public class DelegatingCalendarIteratorTest extends DelegatingIteratorTest<Calendar> {
    private ArrayList<Calendar> data = null;

    @Override
    DelegatingIterator<Calendar> makeNewEmpty() {
        Iterable<Calendar> d = new ArrayList<>();
        return new DelegatingIterator<>(d.iterator());
    }

    @Override
    DelegatingIterator<Calendar> makeNewNonEmpty() {
        data = new ArrayList<>();
        data.add(new GregorianCalendar());
        data.add(new GregorianCalendar());
        return new DelegatingIterator<>(data.iterator());
    }

    @Override
    Calendar firstElement() {
        return data.get(0);
    }

    @Override
    Calendar secondElement() {
        return data.get(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void makeNewWithNull() {
        DelegatingIterator<Calendar> d = new DelegatingIterator<>(null);
        assertNotNull(d);
    }
}
