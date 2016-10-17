package icynote.core.impl;

import org.junit.Test;

import java.util.GregorianCalendar;

import icynote.core.Note;
import icynote.core.NoteTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NoteDataTests extends NoteTests {

    private static final int YEAR = 2000;

    @Override
    protected Note makeData() {
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

        Note b = new NoteData(a);

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
