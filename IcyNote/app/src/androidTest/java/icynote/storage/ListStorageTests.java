package icynote.storage;

import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

import java.util.ArrayList;

import icynote.core.Note;
import icynote.core.Storage;
import icynote.core.StorageTests;
import icynote.core.impl.NoteData;

@RunWith(AndroidJUnit4.class)
public class ListStorageTests extends StorageTests {
    @Override
    protected Storage makeNew() {
        return new ListStorage();
    }

    @Override
    protected Storage makeNewWith(Note n1, Note n2, Note n3) {
        ArrayList<Note> memory = new ArrayList<>();
        memory.add(n1);
        memory.add(n2);
        memory.add(n3);
        return new ListStorage(memory);
    }

    @Override
    protected Note makeNewNote() {
        return new NoteData();
    }
}
