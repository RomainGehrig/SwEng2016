package icynote.core.impl;

import org.junit.Test;

import icynote.core.Note;

import static icynote.core.impl.BoundaryCheckerUtil.checkNotNull;
import static org.junit.Assert.assertSame;

public class BoundaryCheckerUtilTests {

    @Test(expected = IllegalArgumentException.class)
    public void whenArgIsNull() {
        checkNotNull(null);
    }

    @Test
    public void whenArgIsNotNull() {
        Note i = new NoteData();
        Note j = checkNotNull(i);
        assertSame(i, j);
    }
}
