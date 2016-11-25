package icynote.noteproviders.decorators;

import java.util.ArrayList;
import java.util.List;

import icynote.note.Note;
import icynote.note.impl.NoteData;
import icynote.noteproviders.NoteProvider;
import icynote.noteproviders.persistent.ListNoteProvider;
import icynote.noteproviders.templates.NoteProviderTests;
import icynote.ui.utils.NotesAdapter;

/**
 * Created by kl on 24.11.2016.
 */
public class CheckedNoteProviderTest extends NoteProviderTests {

    @Override
    protected NoteProvider makeNew() {
        return new CheckedNoteProvider(new ListNoteProvider());
    }

    @Override
    protected NoteProvider makeNewWith(Note<String> n1, Note<String> n2, Note<String> n3) {
        List<Note<String>> list = new ArrayList<>();
        list.add(n1); list.add(n2); list.add(n3);
        ListNoteProvider listNoteProvider = new ListNoteProvider(list);
        return new CheckedNoteProvider(listNoteProvider);
    }

    @Override
    protected Note<String> makeNewNote() {
        return new NoteData();
    }
}