package icynote.noteproviders.persistent;

import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

import java.util.ArrayList;

import icynote.note.Note;
import icynote.noteproviders.NoteProvider;
import icynote.noteproviders.templates.StorageTests;
import icynote.note.impl.NoteData;

@RunWith(AndroidJUnit4.class)
public class ListStorageTests extends StorageTests {
    @Override
    protected NoteProvider<Note<String>> makeNew() {
        return new ListNoteProvider();
    }

    @Override
    protected NoteProvider<Note<String>> makeNewWith(Note<String> n1, Note<String> n2, Note<String> n3) {
        ArrayList<Note<String>> memory = new ArrayList<>();
        memory.add(n1);
        memory.add(n2);
        memory.add(n3);
        return new ListNoteProvider(memory);
    }

    @Override
    protected Note<String> makeNewNote() {
        return new NoteData();
    }
}
