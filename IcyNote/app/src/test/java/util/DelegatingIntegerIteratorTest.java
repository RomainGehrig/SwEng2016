package util;

import java.util.ArrayList;

public class DelegatingIntegerIteratorTest extends DelegatingIteratorTest<Integer> {
    ArrayList<Integer> data;

    @Override
    DelegatingIterator<Integer> makeNewEmpty() {
        ArrayList<Integer> d = new ArrayList<>();
        return new DelegatingIterator<>(d.iterator());
    }

    @Override
    DelegatingIterator<Integer> makeNewNonEmpty() {
        data = new ArrayList<>();
        data.add(Integer.valueOf(1));
        data.add(Integer.valueOf(2));
        return new DelegatingIterator<>(data.iterator());
    }

    @Override
    Integer firstElement() {
        return data.get(0);
    }

    @Override
    Integer secondElement() {
        return data.get(1);
    }
}
