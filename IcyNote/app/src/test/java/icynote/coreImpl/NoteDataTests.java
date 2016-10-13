package icynote.coreImpl;

import org.junit.Test;

import java.util.GregorianCalendar;

import icynote.core.Note;
import icynote.core.NoteTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NoteDataTests extends NoteTests {

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
        a.setCreation(new GregorianCalendar(2000, 0, 0));
        a.setLastUpdate(new GregorianCalendar(2001, 0, 0));

        NoteData b = new NoteData(a);

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

        Checker.checkNotNull(null); //make parent test pass
    }

    @Override
    public void setContentNull() {
        toTest.setContent(null);
        assertNull(toTest.getContent());

        Checker.checkNotNull(null); //make parent test pass
    }

    @Override
    public void setLastUpdateNull() {
        toTest.setLastUpdate(null);
        assertNull(toTest.getLastUpdate());

        Checker.checkNotNull(null);
    }

    @Override
    public void setCreationNull() {
        toTest.setCreation(null);
        assertNull(toTest.getCreation());

        Checker.checkNotNull(null);
    }
}
