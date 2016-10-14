package icynote.core;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import icynote.coreImpl.NoteData;
import icynote.storage.NullStorageAndCol;
import icynote.storage.NullStorageAndNotCol;

/**
 * Unit tests for {@code NoteProvider} when its delegate storage returns {@code null};
 * should be subclassed to test specific implementations.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public abstract class CoreWithFaultyStorage {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private IcyNoteCore core;

    abstract protected IcyNoteCore makeNew(Storage s);

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
