package icynote.noteproviders.persistent;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

import java.util.ArrayList;

import icynote.note.Note;
import icynote.noteproviders.NoteProvider;
import icynote.noteproviders.templates.StorageTests;
import icynote.note.impl.NoteData;

@RunWith(AndroidJUnit4.class)
public class SQLiteStorageTests extends StorageTests<String, Note<String>> {
    private final String testUser = "testUSER_514307856140378641306406134";
    private final SQLiteNoteProvider instance =
            new SQLiteNoteProvider(InstrumentationRegistry.getTargetContext(), testUser);

    @Override
    protected NoteProvider<Note<String>> makeNew() {
        instance.deleteUser(testUser);
        return instance;
    }

    @Override
    protected NoteProvider<Note<String>> makeNewWith(Note<String> n1, Note<String> n2, Note<String> n3) {
        instance.deleteUser(testUser);
        ArrayList<Note<String>> list = new ArrayList<>(); list.add(n1);list.add(n2);list.add(n3);
        for( Note<String> n : list ){
            Note<String> c = instance.createNote().get();
            n.setId(c.getId());
            instance.persist(n);
        }
        return instance;
    }

    @Override
    protected Note<String> makeNewNote() {
        return new NoteData<>("", "");
    }

    @Override
    protected String makeTitle1() {
        return "n1";
    }

    @Override
    protected String makeTitle2() {
        return "n2";
    }

    @Override
    protected String makeTitle3() {
        return "n3";
    }

    @Override
    protected String makeContent1() {
        return "c1";
    }
}