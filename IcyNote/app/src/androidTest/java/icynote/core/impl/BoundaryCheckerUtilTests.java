package icynote.core.impl;

import org.junit.Test;

import icynote.core.Note;

import icynote.core.impl.BoundaryCheckerUtil;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class BoundaryCheckerUtilTests {

    @Test(expected=AssertionError.class)
    public void createUtil(){
        BoundaryCheckerUtil c = new BoundaryCheckerUtil();
        assertNull(c);
    }
    @Test(expected = IllegalArgumentException.class)
    public void whenArgIsNull() {
        BoundaryCheckerUtil.checkNotNull(null);
    }

    @Test
    public void whenArgIsNotNull() {
        Note i = new NoteData();
        Note j = BoundaryCheckerUtil.checkNotNull(i);
        assertSame(i, j);
    }
}
