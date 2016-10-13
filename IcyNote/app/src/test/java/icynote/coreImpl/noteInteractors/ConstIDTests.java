package icynote.coreImpl.noteInteractors;


import org.junit.Test;

import icynote.core.Note;
import icynote.core.NoteTests;
import icynote.core.Response;
import icynote.coreImpl.NoteData;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

public class ConstIDTests extends NoteTests {
    @Override
    protected Note makeData() {
        return new NoteData();
    }

    @Override
    protected Note addInteractor(Note data) {
        return new NullInputChecker(new ConstID(data));
    }

    @Test
    public void setId() {
        Response r = toTest.setId(0);
        assertNotNull(r);
        assertFalse(r.isPositive());
    }

}