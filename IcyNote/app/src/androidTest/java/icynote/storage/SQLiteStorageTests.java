package icynote.storage;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

import icynote.core.Note;
import icynote.core.Storage;
import icynote.core.StorageTests;
import icynote.core.impl.NoteData;

@RunWith(AndroidJUnit4.class)
public class SQLiteStorageTests extends StorageTests {
    private final String testUser = "testUSER_514307856140378641306406134";
    private SQLiteStorage instance =
            new SQLiteStorage(InstrumentationRegistry.getTargetContext(), testUser);

    @Override
    protected Storage makeNew() {
        instance.deleteUser(testUser);
        return instance;
    }

    @Override
    protected Storage makeNewWith(Note n1, Note n2, Note n3) {
        instance.deleteUser(testUser);
        for( Note n : new Note[]{n1, n2, n3}){
            Note c = instance.createNote().get();
            n.setId(c.getId());
            instance.persist(n);
        }
        return instance;
    }

    @Override
    protected Note makeNewNote() {
        return new NoteData();
    }
}