package icynote.core.impl;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import icynote.core.Note;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;

@RunWith(AndroidJUnit4.class)
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
