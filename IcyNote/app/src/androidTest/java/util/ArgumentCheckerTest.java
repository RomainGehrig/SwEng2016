package util;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kl on 24.11.2016.
 */
public class ArgumentCheckerTest {
    @Test (expected = IllegalArgumentException.class)
    public void IsNullTest() {
        ArgumentChecker.requireNonNull(null);
    }

    @Test
    public void IsNotNullTest() {
        List<String> list = new ArrayList<>();
        assertEquals(list, ArgumentChecker.requireNonNull(list));
    }
}