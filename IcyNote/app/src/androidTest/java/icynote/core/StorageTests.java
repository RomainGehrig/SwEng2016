package icynote.core;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;

/**
 * Unit tests for the {@code Storage} interface;
 * should be subclassed to test specific implementations.
 *
 * @author Mike Bardet
 * @version 1.0
 */
@RunWith(AndroidJUnit4.class)
public abstract class StorageTests extends NoteProviderTests {
    @Test
    public void getNoteDefensiveCopyTest() {
        Note newNote = createNoteAndCheck();
        int id = newNote.getId();
        toTest.persist(newNote);

        Optional<Note> n1 = toTest.getNote(id);
        Optional<Note> n2 = toTest.getNote(id);

        assertTrue(n1.isPresent());
        assertTrue(n2.isPresent());

        assertNotSame(n1.get(), n2.get());
    }

    @Test
    public void getNoteDefensiveCopyTest2() {
        Note newNote = createNoteAndCheck();
        newNote.setTitle("Title");
        int id = newNote.getId();
        toTest.persist(newNote);

        Optional<Note> note = toTest.getNote(id);
        note.get().setTitle("New title");

        Optional<Note> noteModified = toTest.getNote(id);
        assertEquals(noteModified.get().getTitle(), "Title");
    }

    @Test
    public void getNotesDefensiveTest() {
        Note newNote1 = createNoteAndCheck();
        Note newNote2 = createNoteAndCheck();
        Note newNote3 = createNoteAndCheck();

        String title = "Title";
        newNote1.setTitle(title);
        newNote2.setTitle(title);
        newNote3.setTitle(title);

        toTest.persist(newNote1);
        toTest.persist(newNote2);
        toTest.persist(newNote3);

        Iterable<Note> notes = toTest.getNotes(OrderBy.TITLE, OrderType.ASC);

        for (Note n : notes) {
            n.setTitle("New title");
        }

        Iterable<Note> notesModified = toTest.getNotes(OrderBy.TITLE, OrderType.ASC);
        for (Note n : notesModified) {
            assertEquals(n.getTitle(), title);
        }
    }
}