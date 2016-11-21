package icynote.noteproviders.templates;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

import icynote.note.Note;
import icynote.noteproviders.NoteProvider;
import icynote.noteproviders.OrderBy;
import icynote.noteproviders.OrderType;
import icynote.note.Response;
import util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;


/**
 * Unit tests for {@code NoteProvider}; should be subclassed to test specific implementations.
 *
 * @author Julien Harbulot
 * @author Mike Bardet
 * @version 1.0
 */

@RunWith(AndroidJUnit4.class)
@SuppressWarnings("ClassWithTooManyMethods")
public abstract class NoteProviderTests {

    private static final int YEAR = 2000;

    @SuppressWarnings("WeakerAccess") //keep protected because this test class is a template
    protected NoteProvider toTest = null;

    /**
     * Creates a new, empty, NoteProvider instance to be tested.
     */
    protected abstract NoteProvider makeNew();

    /**
     * Creates a new NoteProvider instance containing the provided notes.
     * Every field of the note must be preserved.
     */
    protected abstract NoteProvider makeNewWith(Note<String> n1, Note<String> n2, Note<String> n3);

    /**
     * Creates a new note instance, not contained by the NoteProvider.
     */
    protected abstract Note<String> makeNewNote();

    //-----------------------------------------------------------------------

    @Before
    public void setup() {
        toTest = makeNew();
    }

    //-----------------------------------------------------------------------

    @Test
    public void createNoteTest() {
        createNoteAndCheck();
    }

    @Test
    public void createMultipleNotesTest() {
        Note<String> n1 = createNoteAndCheck();
        Note<String> n2 = createNoteAndCheck();
        //Assert.assertThat(n1.getId(), not(equalTo(n2.getId())));
        assertFalse("", n1.getId() == n2.getId());
    }

    Note<String> createNoteAndCheck() {
        Optional<Note<String>> n = toTest.createNote();
        assertNotNull(n);
        assertTrue("created note is not empty", n.isPresent());
        return n.get();
    }

    //-----------------------------------------------------------------------

    @Test
    public void getExistingNoteTest() {
        Note<String> created = createNoteAndCheck();
        created.setContent("hello world");
        int validId = created.getId();
        toTest.persist(created);

        Optional<Note<String>> n = toTest.getNote(validId);
        assertNotNull(n);
        assertTrue("returned note is present", n.isPresent());
        assertEquals(created.getContent(), n.get().getContent());
    }

    @Test
    public void getNonExistingNoteTest() {
        Optional<Note<String>> n = toTest.getNote(0);
        assertNotNull(n);
        assertFalse("returned note is empty", n.isPresent());
    }

    //-----------------------------------------------------------------------

    @Test
    public void persistWithoutModification() {
        Note<String> n = createNoteAndCheck();
        Response r = toTest.persist(n);
        assertTrue(r.isPositive());
    }

    @Test
    public void persistWithTitleModification() {
        Note<String> n = createNoteAndCheck();
        n.setTitle("new title");
        Response r = toTest.persist(n);
        assertNotNull(r);
        assertTrue(r.isPositive());

        Optional<Note<String>> m = toTest.getNote(n.getId());
        assertTrue("cannot retrieve note", m.isPresent());
        assertEquals(n.getTitle(), m.get().getTitle());
    }

    @Test
    public void persistWithContentModification() {
        Note<String> n = createNoteAndCheck();
        n.setContent("new content");
        Response r = toTest.persist(n);
        assertNotNull(r);
        assertTrue(r.isPositive());

        Optional<Note<String>> m = toTest.getNote(n.getId());
        assertTrue("cannot retrieve note", m.isPresent());
        assertEquals(n.getContent(), m.get().getContent());
    }

    @Test
    public void persistANonExistingNote() {
        Note<String> n = makeNewNote();
        Response r = toTest.persist(n);
        assertNotNull(r);
        assertFalse(r.isPositive());
    }

    @Test
    public void persistThenModifyNote() {
        Note<String> newNote = createNoteAndCheck();
        newNote.setTitle("Title");
        int id = newNote.getId();
        toTest.persist(newNote);

        newNote.setTitle("New title");

        Optional<Note<String>> noteModified = toTest.getNote(id);
        assertTrue(noteModified.isPresent());
        assertEquals(noteModified.get().getTitle(), "Title");
    }

    @Test
    public void noteCreatedButNotPersisted() {
        Note<String> n1 = createNoteAndCheck();
        n1.setTitle("new title");

        Optional<Note<String>> n2 = toTest.getNote(n1.getId());

        assertTrue("the note should be obtainable with get after creation",
                n2.isPresent());

        assertFalse("but if we don't persist it, modifications shouldn't be registered",
                n1.getTitle().equals(n2.get().getTitle()));
    }

    //-----------------------------------------------------------------------


    @Test
    public void deleteAnExistingNote() {
        Note<String> created = createNoteAndCheck();
        created.setContent("hello world");
        int validId = created.getId();
        toTest.persist(created);

        Response response = toTest.delete(validId);
        assertTrue(response.isPositive());

        Optional<Note<String>> note = toTest.getNote(validId);
        assertFalse("returned note is not present", note.isPresent());
    }

    @Test
    public void deleteANonExistingNote() {
        int invalidID = -1;
        assertFalse(toTest.getNote(invalidID).isPresent());

        Response response = toTest.delete(invalidID);
        assertFalse(response.isPositive());
    }

    @Test
    public void deleteDoesNotAffectOtherNotes() {
        Note<String> n1 = makeNewNote();
        n1.setId(0);
        Note<String> n2 = makeNewNote();
        n2.setId(1);
        Note<String> n3 = makeNewNote();
        n3.setId(2);
        toTest = makeNewWith(n1, n2, n3);

        int invalidID = -1;
        assertFalse(toTest.getNote(invalidID).isPresent());

        int sizeBeforeDelete = size(toTest.getNotes(OrderBy.TITLE, OrderType.ASC));

        Response response = toTest.delete(invalidID);
        assertFalse(response.isPositive());

        int sizeAfterDelete = size(toTest.getNotes(OrderBy.TITLE, OrderType.ASC));

        assertEquals(sizeBeforeDelete, sizeAfterDelete);
    }

    //-----------------------------------------------------------------------

    @Test
    public void getNotesDefensiveCopy() {
        //create a few notes
        assertTrue(toTest.createNote().isPresent());
        assertTrue(toTest.createNote().isPresent());
        assertTrue(toTest.createNote().isPresent());

        Iterable<Note<String>> notes = toTest.getNotes(OrderBy.TITLE, OrderType.ASC);
        Iterable<Note<String>> notes2 = toTest.getNotes(OrderBy.TITLE, OrderType.ASC);

        assertNotSame(notes, notes2);
    }

    @Test
    public void getNotesIterable() {
        //create a few notes
        assertTrue("note created", toTest.createNote().isPresent());
        assertTrue("note2 created", toTest.createNote().isPresent());
        assertTrue("note3 created", toTest.createNote().isPresent());


        Iterable<Note<String>> notes = toTest.getNotes(OrderBy.TITLE, OrderType.ASC);

        int sizeBeforeRemove = size(notes);

        Iterator<Note<String>> it = notes.iterator();
        assertTrue("it has next", it.hasNext());

        it.next();
        it.remove();

        int sizeAfterRemove = size(notes);
        assertEquals(sizeBeforeRemove, sizeAfterRemove + 1);

        Iterable<Note<String>> notes2 = toTest.getNotes(OrderBy.TITLE, OrderType.ASC);
        assertEquals(sizeBeforeRemove, size(notes2));
    }

    @Test
    public void getNotesByTitleAscTest() {
        getNotes(OrderBy.TITLE, new FieldExtractor() {
            @Override
            public Comparable<String> getField(Note<String> n) {
                return n.getTitle();
            }
        }, true);
    }

    @Test
    public void getNotesByTitleDscTest() {
        getNotes(OrderBy.TITLE, new FieldExtractor() {
            @Override
            public Comparable<String> getField(Note<String> n) {
                return n.getTitle();
            }
        }, false);
    }

    @Test
    public void getNotesByCreationAscTest() {
        getNotes(OrderBy.CREATION, new FieldExtractor() {
            @Override
            public Comparable<Calendar> getField(Note<String> n) {
                return n.getCreation();
            }
        }, true);
    }

    @Test
    public void getNotesByCreationDscTest() {
        getNotes(OrderBy.CREATION, new FieldExtractor() {
            @Override
            public Comparable<Calendar> getField(Note<String> n) {
                return n.getCreation();
            }
        }, false);
    }

    @Test
    public void getNotesByUpdateAscTest() {
        getNotes(OrderBy.LAST_UPDATE, new FieldExtractor() {
            @Override
            public Comparable<Calendar> getField(Note<String> n) {
                return n.getLastUpdate();
            }
        }, true);
    }

    @Test
    public void getNotesByUpdateDscTest() {
        getNotes(OrderBy.LAST_UPDATE, new FieldExtractor() {
            @Override
            public Comparable<Calendar> getField(Note<String> n) {
                return n.getLastUpdate();
            }
        }, false);
    }

    //-----------------------------------------------------------------------

    private void getNotes(OrderBy by, final FieldExtractor get, final boolean asc) {
        Note<String> n1 = makeNewNote();
        Note<String> n2 = makeNewNote();
        Note<String> n3 = makeNewNote();

        GregorianCalendar d = new GregorianCalendar(YEAR, 1, 1);

        n1.setTitle("n1");
        n2.setTitle("n2");
        n3.setTitle("n3");

        n3.setCreation(add(d, 1));
        n2.setCreation(add(d, 2));
        n1.setCreation(add(d, 3));

        n2.setLastUpdate(add(d, 4));
        n3.setLastUpdate(add(d, 5));
        n1.setLastUpdate(add(d, 6));

        toTest = makeNewWith(n1, n2, n3);


        OrderType tpe = (asc) ? OrderType.ASC : OrderType.DSC;
        Iterable<Note<String>> notes = toTest.getNotes(by, tpe);
        assertNotNull("note iterable should not be null", notes);

        checkOrder(notes.iterator(), new OrderPredicate<Note<String>>() {
            @Override
            public boolean ordered(Note<String> lhs, Note<String> rhs) {

                @SuppressWarnings("unchecked")
                int comp = get.getField(lhs).compareTo(get.getField(rhs));
                return (asc) ? comp <= 0 : comp >= 0;
            }
        });
    }

    private <T> void checkOrder(Iterator<T> it, OrderPredicate<T> orderPredicate) {
        if (!it.hasNext()) {
            return;
        }

        T previous;
        T current = it.next();
        assertNotNull("Note in iterable should not be null", current);

        while (it.hasNext()) {

            previous = current;
            current = it.next();
            assertNotNull("Note in iterable should not be null", current);

            boolean good = orderPredicate.ordered(previous, current);
            assertTrue(good);
        }
    }


    @SuppressWarnings("UseOfClone")
    private GregorianCalendar add(GregorianCalendar d, int years) {
        GregorianCalendar n = (GregorianCalendar) d.clone();
        n.add(GregorianCalendar.YEAR, years);
        return n;
    }

    private int size(Iterable<Note<String>> collection) {
        int sz = 0;
        for (Note<String> n : collection) {
            ++sz;
        }
        return sz;
    }

    private interface FieldExtractor {
        @SuppressWarnings("rawtypes") //to use both String and Calendar
        Comparable getField(Note<String> n);
    }

    private interface OrderPredicate<T> {
        boolean ordered(T lhs, T rhs);
    }
}
