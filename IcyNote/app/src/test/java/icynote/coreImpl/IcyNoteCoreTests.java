package icynote.coreImpl;


import org.junit.Test;

import java.util.ArrayList;

import icynote.core.Note;
import icynote.core.NoteProvider;
import icynote.core.NoteProviderTests;
import icynote.core.Storage;
import icynote.coreImpl.noteInteractors.NoteInteractor;
import icynote.coreImpl.noteInteractors.NoteInteractorFactory;
import icynote.storage.ListStorage;
import util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IcyNoteCoreTests extends NoteProviderTests {

    @Override
    protected NoteProvider makeNew() {
        return new IcyNoteCore(new ListStorage());
    }

    @Override
    protected NoteProvider makeNewWith(Note n1, Note n2, Note n3) {
        ArrayList<Note> memory = new ArrayList<>();
        memory.add(n1);
        memory.add(n2);
        memory.add(n3);
        Storage storage = new ListStorage(memory);
        return new IcyNoteCore(storage);
    }

    @Override
    protected Note makeNewNote() {
        return new NoteData();
    }

    @Test
    public void stackTest() {
        final String hackedTitle = "test:title_hacked";

        IcyNoteCore core = new IcyNoteCore(new ListStorage());
        core.stack(new NoteInteractorFactory() {
            @Override
            public Note make(Note delegate) {
                return new NoteInteractor(delegate) {
                    @Override
                    public String getTitle() {
                        return hackedTitle;
                    }
                };
            }
        });

        Optional<Note> note = core.createNote();

        assertTrue(note.isPresent());
        assertEquals(note.get().getTitle(), hackedTitle);
    }
}
