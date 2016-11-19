package icynote.core.impl.interactors;


import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

import icynote.core.Note;
import icynote.core.NoteTests;
import icynote.core.Response;
import icynote.core.impl.NoteData;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class ConstIDInteractorTests extends NoteTests {
    @Override
    protected Note makeData() {
        return new NoteData();
    }

    @Override
    protected Note addInteractor(Note data) {
        return new NullInputInteractor(new ConstIdInteractor(data));
    }

    @Override
    public void setId() {
        Response r = toTest.setId(0);
        assertNotNull(r);
        assertFalse(r.isPositive());
    }

}