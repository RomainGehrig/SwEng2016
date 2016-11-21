package icynote.noteproviders.templates;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.GregorianCalendar;

import icynote.note.Note;
import icynote.note.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;


/**
 * Unit tests for a {@code Note}; may be subclassed and
 * specialized to test specific implementations.
 */

@SuppressWarnings("CanBeFinal")
@RunWith(AndroidJUnit4.class)
public abstract class NoteTests {

    private static final int YEAR = 2000;
    // keep those attributes protected, some interactor might have a validation rule
    // that needs a different value than those proposed here
    protected Note<String> toTest = null;
    protected String originalTitle = "NoteTests.title";
    protected String originalContent = "NoteTests.content";
    protected GregorianCalendar originalCreation = new GregorianCalendar(YEAR, 1, 1);
    protected GregorianCalendar originalUpdate = new GregorianCalendar(YEAR + 1, 1, 1);

    protected Note<String> addInteractor(Note<String> data) {
        return data;
    }

    protected abstract Note<String> makeData();

    //---------------------------------------------------------------------------------

    @Before
    public void reset() {
        toTest = makeData();
        toTest.setTitle(originalTitle);
        toTest.setContent(originalContent);
        toTest.setCreation(originalCreation);
        toTest.setLastUpdate(originalUpdate);
        toTest = addInteractor(toTest);
    }

    //---------------------------------------------------------------------------------

    @Test
    public void setId() {
        int id = 100;
        Response r = toTest.setId(id);

        assertNotNull("response is not null", r);
        assertTrue("response is positive", r.isPositive());
        assertEquals("check new value", toTest.getId(), id);
    }

    @Test
    public void setCreation() {
        GregorianCalendar d = new GregorianCalendar();
        Response r = toTest.setCreation(d);

        assertNotNull("response is not null", r);
        assertTrue("response is positive", r.isPositive());
        assertEquals("check new value", toTest.getCreation(), d);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setCreationNull() {
        toTest.setCreation(null);
    }


    @Test
    public void setLastUpdate() {
        GregorianCalendar d = new GregorianCalendar();
        Response r = toTest.setLastUpdate(d);

        assertNotNull("response is not null", r);
        assertTrue("response is positive", r.isPositive());
        assertEquals("check new value", toTest.getLastUpdate(), d);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setLastUpdateNull() {
        toTest.setLastUpdate(null);
    }


    @Test
    public void setTitle() {
        String newTitle = "another title";
        Response r = toTest.setTitle(newTitle);

        assertTrue("check setTitle response", r.isPositive());
        assertEquals("check new title value", newTitle, toTest.getTitle());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setTitleNull() {
        toTest.setTitle(null);
    }


    @Test
    public void setContent() {
        String newContent = "a better content";
        Response r = toTest.setContent(newContent);

        assertTrue(r.isPositive());
        assertEquals(newContent, toTest.getContent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setContentNull() {
        toTest.setContent(null);
    }

    @Test
    public void getCreationDefensiveCopy() {
        GregorianCalendar d = toTest.getCreation();
        GregorianCalendar e = toTest.getCreation();
        assertNotSame(d, e);
    }

    @Test
    public void getModificationDefensiveCopy() {
        GregorianCalendar d = toTest.getLastUpdate();
        GregorianCalendar e = toTest.getLastUpdate();
        assertNotSame(d, e);
    }
}