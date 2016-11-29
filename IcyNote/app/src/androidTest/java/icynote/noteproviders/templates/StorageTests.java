package icynote.noteproviders.templates;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import icynote.note.Note;
import icynote.noteproviders.OrderBy;
import icynote.noteproviders.OrderType;
import util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;

/**
 * Unit tests for the {@code NoteProvider} interface;
 * should be subclassed to test specific implementations.
 *
 * @author Mike Bardet
 * @version 1.0
 */
@RunWith(AndroidJUnit4.class)
public abstract class StorageTests<
        S extends Comparable<S>,
        N extends Note<S>>
        extends NoteProviderTests<S, N>
{
    @Test
    public void getNoteDefensiveCopyTest() {
        N newNote = createNoteAndCheck();
        int id = newNote.getId();
        toTest.persist(newNote);

        Optional<N> n1 = toTest.getNote(id);
        Optional<N> n2 = toTest.getNote(id);

        assertTrue(n1.isPresent());
        assertTrue(n2.isPresent());

        assertNotSame(n1.get(), n2.get());
    }

    @Test
    public void getNoteDefensiveCopyTest2() {
        N newNote = createNoteAndCheck();
        newNote.setTitle(makeTitle1());
        int id = newNote.getId();
        toTest.persist(newNote);

        Optional<N> note = toTest.getNote(id);
        note.get().setTitle(makeTitle2());

        Optional<N> noteModified = toTest.getNote(id);
        assertEquals(noteModified.get().getTitle(), makeTitle1());
    }

    @Test
    public void getNotesDefensiveTest() {
        N newNote1 = createNoteAndCheck();
        N newNote2 = createNoteAndCheck();
        N newNote3 = createNoteAndCheck();

        S title = makeTitle1();
        newNote1.setTitle(title);
        newNote2.setTitle(title);
        newNote3.setTitle(title);

        toTest.persist(newNote1);
        toTest.persist(newNote2);
        toTest.persist(newNote3);

        Iterable<N> notes = toTest.getNotes(OrderBy.TITLE, OrderType.ASC);

        for (N n : notes) {
            n.setTitle(makeTitle2());
        }

        Iterable<N> notesModified = toTest.getNotes(OrderBy.TITLE, OrderType.ASC);
        for (Note<S> n : notesModified) {
            assertEquals(n.getTitle(), title);
        }
    }
}