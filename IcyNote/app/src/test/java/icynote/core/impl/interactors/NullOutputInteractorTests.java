package icynote.core.impl.interactors;

import org.junit.Test;

import java.util.GregorianCalendar;

import icynote.core.Note;
import icynote.core.impl.NoteData;

public class NullOutputInteractorTests {
    private final Note nullReturner = new NullNoteData();
    private final Note toTest = new NullOutputInteractor(nullReturner);

    @Test(expected = IllegalArgumentException.class)
    public void getTitle() {
        toTest.getTitle();
    }

    //-----------------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void getContent() {
        toTest.getContent();
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCreation() {
        toTest.getCreation();
    }

    @Test(expected = IllegalArgumentException.class)
    public void getLastUpdate() {
        toTest.getLastUpdate();
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
