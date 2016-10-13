package icynote.coreImpl.noteInteractors;

import org.junit.Test;

import java.util.GregorianCalendar;

import icynote.core.Note;
import icynote.coreImpl.NoteData;

public class NullOutputCheckerTests {
    private final Note nullReturner = new NoteData() {
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
    };

    private final Note toTest = new NullOutputChecker(nullReturner);

    //-----------------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void getTitle() {
        toTest.getTitle();
    }

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

}
