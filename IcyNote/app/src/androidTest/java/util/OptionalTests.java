package util;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.NoSuchElementException;


@RunWith(AndroidJUnit4.class)
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