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
public abstract class NoteProviderTests<S extends Comparable<S>, N extends Note<S>> {

    private static final int YEAR = 2000;

    @SuppressWarnings("WeakerAccess") //keep protected because this test class is a template
    protected NoteProvider<N> toTest = null;

    /**
     * Creates a new, empty, NoteProvider<N> instance to be tested.
     */
    protected abstract NoteProvider<N> makeNew();

    /**
     * Creates a new NoteProvider<N> instance containing the provided notes.
     * Every field of the note must be preserved.
     */
    protected abstract NoteProvider<N> makeNewWith(N n1, N n2, N n3);

    /**
     * Creates a new note instance, not contained by the NoteProvider.
     */
    protected abstract N makeNewNote();

    protected abstract S makeTitle1();
    protected abstract S makeTitle2();
    protected abstract S makeTitle3();

    protected abstract S makeContent1();

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
        N n1 = createNoteAndCheck();
        N n2 = createNoteAndCheck();
        //Assert.assertThat(n1.getId(), not(equalTo(n2.getId())));
        assertFalse("", n1.getId() == n2.getId());
    }

    N createNoteAndCheck() {
        Optional<N> n = toTest.createNote();
        assertNotNull(n);
        assertTrue("created note is not empty", n.isPresent());
        return n.get();
    }

    //-----------------------------------------------------------------------

    @Test
    public void getExistingNoteTest() {
        N created = createNoteAndCheck();
        created.setContent(makeContent1());
        int validId = created.getId();
        toTest.persist(created);

        Optional<N> n = toTest.getNote(validId);
        assertNotNull(n);
        assertTrue("returned note is present", n.isPresent());
        assertEquals(created.getContent(), n.get().getContent());
    }

    @Test
    public void getNonExistingNoteTest() {
        Optional<N> n = toTest.getNote(0);
        assertNotNull(n);
        assertFalse("returned note is empty", n.isPresent());
    }

    //-----------------------------------------------------------------------

    @Test
    public void persistWithoutModification() {
        N n = createNoteAndCheck();
        Response r = toTest.persist(n);
        assertTrue(r.isPositive());
    }

    @Test
    public void persistWithTitleModification() {
        N n = createNoteAndCheck();
        n.setTitle(makeTitle1());
        Response r = toTest.persist(n);
        assertNotNull(r);
        assertTrue(r.isPositive());

        Optional<N> m = toTest.getNote(n.getId());
        assertTrue("cannot retrieve note", m.isPresent());
        assertEquals(n.getTitle(), m.get().getTitle());
    }

    @Test
    public void persistWithContentModification() {
        N n = createNoteAndCheck();
        n.setContent(makeContent1());
        Response r = toTest.persist(n);
        assertNotNull(r);
        assertTrue(r.isPositive());

        Optional<N> m = toTest.getNote(n.getId());
        assertTrue("cannot retrieve note", m.isPresent());
        assertEquals(n.getContent(), m.get().getContent());
    }

    @Test
    public void persistANonExistingNote() {
        N n = makeNewNote();
        Response r = toTest.persist(n);
        assertNotNull(r);
        assertFalse(r.isPositive());
    }

    @Test
    public void persistThenModifyNote() {
        N newNote = createNoteAndCheck();
        newNote.setTitle(makeTitle1());
        int id = newNote.getId();
        toTest.persist(newNote);

        newNote.setTitle(makeTitle2());

        Optional<N> noteModified = toTest.getNote(id);
        assertTrue(noteModified.isPresent());
        assertEquals(noteModified.get().getTitle(), makeTitle1());
    }


    // FIXME
    /*
    @Test
    public void noteCreatedButNotPersisted() {
        N n1 = createNoteAndCheck();
        n1.setTitle(makeTitle1());

        Optional<N> n2 = toTest.getNote(n1.getId());

        assertTrue("the note should be obtainable with get after creation",
                n2.isPresent());

        assertFalse("but if we don't persist it, modifications shouldn't be registered",
                n1.getTitle().equals(n2.get().getTitle()));
    }//*/

    //-----------------------------------------------------------------------


    @Test
    public void deleteAnExistingNote() {
        N created = createNoteAndCheck();
        created.setContent(makeContent1());
        int validId = created.getId();
        toTest.persist(created);

        Response response = toTest.delete(validId);
        assertTrue(response.isPositive());

        Optional<N> note = toTest.getNote(validId);
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
        N n1 = makeNewNote();
        n1.setId(0);
        N n2 = makeNewNote();
        n2.setId(1);
        N n3 = makeNewNote();
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

        Iterable<N> notes = toTest.getNotes(OrderBy.TITLE, OrderType.ASC);
        Iterable<N> notes2 = toTest.getNotes(OrderBy.TITLE, OrderType.ASC);

        assertNotSame(notes, notes2);
    }

    // FIXME
    /*
    @Test
    public void getNotesIterable() {
        //create a few notes
        assertTrue("note created", toTest.createNote().isPresent());
        assertTrue("note2 created", toTest.createNote().isPresent());
        assertTrue("note3 created", toTest.createNote().isPresent());


        Iterable<N> notes = toTest.getNotes(OrderBy.TITLE, OrderType.ASC);

        int sizeBeforeRemove = size(notes);

        Iterator<N> it = notes.iterator();
        assertTrue("it has next", it.hasNext());

        it.next();
        it.remove();

        int sizeAfterRemove = size(notes);
        assertEquals(sizeBeforeRemove, sizeAfterRemove + 1);

        Iterable<N> notes2 = toTest.getNotes(OrderBy.TITLE, OrderType.ASC);
        assertEquals(sizeBeforeRemove, size(notes2));
    }//*/

    @Test
    public void getNotesByTitleAscTest() {
        getNotes(OrderBy.TITLE, new FieldExtractor<S, N>() {
            @Override
            public Comparable<S> getField(N n) {
                return n.getTitle();
            }
        }, true);
    }

    @Test
    public void getNotesByTitleDscTest() {
        getNotes(OrderBy.TITLE, new FieldExtractor<S, N>() {
            @Override
            public Comparable<S> getField(N n) {
                return n.getTitle();
            }
        }, false);
    }

    @Test
    public void getNotesByCreationAscTest() {
        getNotes(OrderBy.CREATION, new FieldExtractor<S,N>() {
            @Override
            public Comparable<Calendar> getField(N n) {
                return n.getCreation();
            }
        }, true);
    }

    @Test
    public void getNotesByCreationDscTest() {
        getNotes(OrderBy.CREATION, new FieldExtractor<S, N>() {
            @Override
            public Comparable<Calendar> getField(N n) {
                return n.getCreation();
            }
        }, false);
    }

    @Test
    public void getNotesByUpdateAscTest() {
        getNotes(OrderBy.LAST_UPDATE, new FieldExtractor<S, N>() {
            @Override
            public Comparable<Calendar> getField(N n) {
                return n.getLastUpdate();
            }
        }, true);
    }

    @Test
    public void getNotesByUpdateDscTest() {
        getNotes(OrderBy.LAST_UPDATE, new FieldExtractor<S, N>() {
            @Override
            public Comparable<Calendar> getField(N n) {
                return n.getLastUpdate();
            }
        }, false);
    }

    //-----------------------------------------------------------------------

    private void getNotes(OrderBy by, final FieldExtractor<S, N> get, final boolean asc) {
        N n1 = makeNewNote();
        N n2 = makeNewNote();
        N n3 = makeNewNote();

        GregorianCalendar d = new GregorianCalendar(YEAR, 1, 1);

        n1.setTitle(makeTitle1());
        n2.setTitle(makeTitle2());
        n3.setTitle(makeTitle3());

        n3.setCreation(add(d, 1));
        n2.setCreation(add(d, 2));
        n1.setCreation(add(d, 3));

        n2.setLastUpdate(add(d, 4));
        n3.setLastUpdate(add(d, 5));
        n1.setLastUpdate(add(d, 6));

        toTest = makeNewWith(n1, n2, n3);


        OrderType tpe = (asc) ? OrderType.ASC : OrderType.DSC;
        Iterable<N> notes = toTest.getNotes(by, tpe);
        assertNotNull("note iterable should not be null", notes);

        checkOrder(notes.iterator(), new OrderPredicate<N>() {
            @Override
            public boolean ordered(N lhs, N rhs) {

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

    private int size(Iterable<N> collection) {
        int sz = 0;
        for (N n : collection) {
            ++sz;
        }
        return sz;
    }

    private interface FieldExtractor<S, N extends Note<S>> {
        @SuppressWarnings("rawtypes") //to use both String and Calendar
        Comparable getField(N n);
    }

    private interface OrderPredicate<T> {
        boolean ordered(T lhs, T rhs);
    }
}
