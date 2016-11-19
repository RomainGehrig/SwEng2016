package icynote.core.impl;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import icynote.core.IcyNoteCore;
import icynote.core.Note;
import icynote.core.OrderBy;
import icynote.core.OrderType;
import icynote.core.Storage;
import icynote.storage.NullStorageAndCol;
import icynote.storage.NullStorageAndNotCol;

@RunWith(AndroidJUnit4.class)
public class IcyNoteCoreWithFaultyTests {

    protected IcyNoteCore makeNew(Storage s) {
        return new IcyNoteCoreImpl(s);
    }

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    private IcyNoteCore core = null;


    @Before
    public void reset() {
        thrown.expect(IllegalArgumentException.class);
        Storage faulty = new NullStorageAndCol();
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
        core = makeNew(new NullStorageAndNotCol());
        Iterable<Note> l = core.getNotes(OrderBy.TITLE, OrderType.ASC);
        l.iterator().next();
    }

    @Test
    public void persist() {
        core.persist(new NoteData());
    }

    @Test
    public void delete() {
        core.delete(0);
    }
}
