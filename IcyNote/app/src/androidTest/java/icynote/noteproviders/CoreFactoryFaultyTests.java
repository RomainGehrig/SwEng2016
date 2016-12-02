package icynote.noteproviders;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import icynote.note.Note;
import icynote.note.impl.NoteData;
import icynote.noteproviders.impl.Factory;
import icynote.noteproviders.stubs.NullNoteProviderAndCol;
import icynote.noteproviders.stubs.NullNoteProviderAndNotCol;

@RunWith(AndroidJUnit4.class)
public class CoreFactoryFaultyTests {

    protected NoteProvider<Note<String>> makeNew(NoteProvider<Note<String>> s) {
        return Factory.addNullChecks(s);
    }

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    private NoteProvider<Note<String>> core = null;


    @Before
    public void reset() {
        thrown.expect(IllegalArgumentException.class);
        NoteProvider<Note<String>> faulty = new NullNoteProviderAndCol();
        core = makeNew(faulty);
    }

    @Test
    public void createNote() {
        core.createNote();
    }

    @Test
    public void getNote() {
        core.getNote(0);
    }

    @Test
    public void getNotes() {
        core.getNotes(OrderBy.TITLE, OrderType.ASC);
    }

    @Test
    public void getNotesWhenIterableContainsNull() {
        core = makeNew(new NullNoteProviderAndNotCol());
        Iterable<Note<String>> l = core.getNotes(OrderBy.TITLE, OrderType.ASC);
        l.iterator().next();
    }

    @Test
    public void persist() {
        core.persist(new NoteData<>("", ""));
    }

    @Test
    public void delete() {
        core.delete(0);
    }
}
