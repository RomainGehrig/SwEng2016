package icynote.core;

import org.junit.Before;
import org.junit.Test;

import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;


/**
 * Unit tests for a {@code Note}; may be subclassed and
 * specialized to test specific implementations.
 */
public abstract class NoteTests {

    protected Note toTest;
    protected int id = 0;
    protected String originalTitle = "NoteTests.title";
    protected String originalContent = "NoteTests.content";
    protected GregorianCalendar originalCreation = new GregorianCalendar(2000, 1, 1);
    protected GregorianCalendar originalUpdate = new GregorianCalendar(2001, 1, 1);

    protected Note addInteractor(Note data) {
        return data;
    }

    protected abstract Note makeData();

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