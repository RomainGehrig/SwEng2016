package icynote.core.impl.interactors;

import org.junit.Test;

import java.util.GregorianCalendar;

import icynote.core.Note;
import icynote.core.impl.NoteData;

public class NullOutputInteractorTests {
    private final Note toTest_Null = new NullOutputInteractor(new NullNoteData());
    private final Note toTest_NotNull = new NullOutputInteractor(new NoteData());

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

    private static class NullNoteData extends NoteData {
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
