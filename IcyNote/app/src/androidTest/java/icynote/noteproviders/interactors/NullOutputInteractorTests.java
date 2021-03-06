package icynote.noteproviders.interactors;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.GregorianCalendar;

import icynote.note.Note;
import icynote.note.impl.NoteData;
import icynote.note.decorators.NullOutput;

@RunWith(AndroidJUnit4.class)
public class NullOutputInteractorTests {
    private final Note<String> toTest_Null = new NullOutput<>(new NullNoteData());
    private final Note<String> toTest_NotNull = new NullOutput<>(new NoteData<>("", ""));

    //-----------------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void getTitleNull() {
        toTest_Null.getTitle();
    }

    @Test(expected = IllegalArgumentException.class)
    public void getContentNull() {
        toTest_Null.getContent();
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCreationNull() {
        toTest_Null.getCreation();
    }

    @Test(expected = IllegalArgumentException.class)
    public void getLastUpdateNull() {
        toTest_Null.getLastUpdate();
    }

    //-----------------------------------------------------------------------------

    @Test
    public void getTitle() {
        toTest_NotNull.getTitle();
    }

    @Test
    public void getContent() {
        toTest_NotNull.getContent();
    }

    @Test
    public void getCreation() {
        toTest_NotNull.getCreation();
    }

    @Test
    public void getLastUpdate() {
        toTest_NotNull.getLastUpdate();
    }

    private static class NullNoteData extends NoteData<String> {
        private NullNoteData() {
            super("", "");
        }

        @Override
        public String getTitle() {
            return null;
        }

        @Override
        public String getContent() {
            return null;
        }

        @Override
        public GregorianCalendar getCreation() {
            return null;
        }

        @Override
        public GregorianCalendar getLastUpdate() {
            return null;
        }
    }

}
