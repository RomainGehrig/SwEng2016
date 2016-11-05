package util;

import org.junit.Test;

import java.util.NoSuchElementException;

public class OptionalTests {
    @Test(expected = IllegalArgumentException.class)
    public void ofWhenNull() throws Exception {
        Optional.of(null);
    }

    @Test(expected = NoSuchElementException.class)
    public void get() throws Exception {
        Optional.empty().get();
    }

}