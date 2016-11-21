package icynote.noteproviders.interactors;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import icynote.note.Note;
import icynote.note.decorators.NoteDecoratorFactory;
import icynote.note.decorators.NoteDecoratorTemplate;
import icynote.noteproviders.decorators.NoteWithInteractorsProvider;
import icynote.noteproviders.NoteProvider;
import icynote.noteproviders.templates.NoteProviderTests;
import icynote.note.impl.NoteData;
import icynote.noteproviders.persistent.ListNoteProvider;
import util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@SuppressWarnings("ReturnOfInnerClass")
@RunWith(AndroidJUnit4.class)
public class NoteWithItrProviderTests extends NoteProviderTests {

    @Override
    protected NoteProvider<Note<String>> makeNew() {
        return new NoteWithInteractorsProvider<String>(new ListNoteProvider());
    }

    @Override
    protected NoteProvider<Note<String>> makeNewWith(Note<String> n1, Note<String> n2, Note<String> n3) {
        ArrayList<Note<String>> memory = new ArrayList<>();
        memory.add(n1);
        memory.add(n2);
        memory.add(n3);
        NoteProvider<Note<String>> noteProvider = new ListNoteProvider(memory);
        return new NoteWithInteractorsProvider<String>(noteProvider);
    }

    @Override
    protected Note<String> makeNewNote() {
        return new NoteData();
    }

    @Test
    public void stackTest() {
        final String hackedTitle = "test:title_hacked";

        NoteWithInteractorsProvider<String> core = new NoteWithInteractorsProvider<String>(new ListNoteProvider());
        core.stack(new NoteDecoratorFactory<String>() {
            @Override
            public Note<String> make(Note<String> delegate) {
                return new NoteDecoratorTemplate<String>(delegate) {
                    @Override
                    public String getTitle() {
                        return hackedTitle;
                    }
                };
            }
        });

        Optional<Note<String>> note = core.createNote();

        assertTrue(note.isPresent());
        assertEquals(note.get().getTitle(), hackedTitle);
    }
}
