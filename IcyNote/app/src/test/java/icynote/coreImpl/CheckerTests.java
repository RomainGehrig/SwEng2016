package icynote.coreImpl;

import org.junit.Test;

import icynote.core.Note;

import static icynote.coreImpl.Checker.checkNotNull;
import static org.junit.Assert.assertSame;

public class CheckerTests {

    @Test
    public void checkerExists() {
        new Checker();//this is for test coverage
    }

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
