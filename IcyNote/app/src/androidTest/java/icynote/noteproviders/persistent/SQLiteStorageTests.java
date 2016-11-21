package icynote.noteproviders.persistent;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

import icynote.note.Note;
import icynote.noteproviders.NoteProvider;
import icynote.noteproviders.templates.StorageTests;
import icynote.note.impl.NoteData;

@RunWith(AndroidJUnit4.class)
public class SQLiteStorageTests extends StorageTests {
    private final String testUser = "testUSER_514307856140378641306406134";
    private SQLiteNoteProvider instance =
            new SQLiteNoteProvider(InstrumentationRegistry.getTargetContext(), testUser);

    @Override
    protected NoteProvider<Note<String>> makeNew() {
        instance.deleteUser(testUser);
        return instance;
    }

    @Override
    protected NoteProvider<Note<String>> makeNewWith(Note<String> n1, Note<String> n2, Note<String> n3) {
        instance.deleteUser(testUser);
        for( Note<String> n : new Note[]{n1, n2, n3}){
            Note<String> c = instance.createNote().get();
            n.setId(c.getId());
            instance.persist(n);
        }
        return instance;
    }

    @Override
    protected Note<String> makeNewNote() {
        return new NoteData();
    }
}