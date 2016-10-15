package icynote.core.impl;


import org.junit.Test;

import java.util.ArrayList;

import icynote.core.IcyNoteCore;
import icynote.core.Note;
import icynote.core.NoteProviderTests;
import icynote.core.Storage;
import icynote.core.impl.interactors.NoteInteractorFactory;
import icynote.core.impl.interactors.NoteInteractorTemplate;
import icynote.storage.ListStorage;
import util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("ReturnOfInnerClass")
public class IcyNoteCoreImplTests extends NoteProviderTests {

    @Override
    protected IcyNoteCore makeNew() {
        return new IcyNoteCoreImpl(new ListStorage());
    }

    @Override
    protected IcyNoteCore makeNewWith(Note n1, Note n2, Note n3) {
        ArrayList<Note> memory = new ArrayList<>();
        memory.add(n1);
        memory.add(n2);
        memory.add(n3);
        Storage storage = new ListStorage(memory);
        return new IcyNoteCoreImpl(storage);
    }

    @Override
    protected Note makeNewNote() {
        return new NoteData();
    }

    @Test
    public void stackTest() {
        final String hackedTitle = "test:title_hacked";

        IcyNoteCoreImpl core = new IcyNoteCoreImpl(new ListStorage());
        core.stack(new NoteInteractorFactory() {
            @Override
            public Note make(Note delegate) {
                return new NoteInteractorTemplate(delegate) {
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
