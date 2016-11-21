package icynote.noteproviders.common;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.GregorianCalendar;

import icynote.note.common.BoundaryCheckerUtil;
import icynote.note.impl.NoteData;
import icynote.note.Note;
import icynote.noteproviders.templates.NoteTests;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;


@RunWith(AndroidJUnit4.class)
public class NoteDataTests extends NoteTests {

    private static final int YEAR = 2000;

    @Override
    protected Note<String> makeData() {
        return new NoteData();
    }

    @Test
    public void noteDataCanBeCopied() {
        NoteData a = new NoteData();
        a.setId(0);
        a.setTitle("hello");
        a.setContent("hello2");
        a.setCreation(new GregorianCalendar(YEAR, 0, 0));
        a.setLastUpdate(new GregorianCalendar(YEAR + 1, 0, 0));

        Note<String> b = new NoteData(a);

        assertEquals(a.getCreation(), b.getCreation());
        assertEquals(a.getLastUpdate(), b.getLastUpdate());
        assertEquals(a.getId(), b.getId());
        assertEquals(a.getTitle(), b.getTitle());
        assertEquals(a.getContent(), b.getContent());
    }

    @Override
    public void setTitleNull() {
        toTest.setTitle(null);
        assertNull(toTest.getTitle());

        BoundaryCheckerUtil.checkNotNull(null); //make parent test pass
    }

    @Override
    public void setContentNull() {
        toTest.setContent(null);
        assertNull(toTest.getContent());

        BoundaryCheckerUtil.checkNotNull(null); //make parent test pass
    }

    @Override
    public void setLastUpdateNull() {
        toTest.setLastUpdate(null);
        assertNull(toTest.getLastUpdate());

        BoundaryCheckerUtil.checkNotNull(null);
    }

    @Override
    public void setCreationNull() {
        toTest.setCreation(null);
        assertNull(toTest.getCreation());

        BoundaryCheckerUtil.checkNotNull(null);
    }
}
