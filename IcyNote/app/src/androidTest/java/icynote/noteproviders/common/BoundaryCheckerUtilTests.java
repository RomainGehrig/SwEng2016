package icynote.noteproviders.common;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import icynote.note.common.BoundaryCheckerUtil;
import icynote.note.impl.NoteData;
import icynote.note.Note;

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
        Note<String> i = new NoteData();
        Note<String> j = BoundaryCheckerUtil.checkNotNull(i);
        assertSame(i, j);
    }
}
