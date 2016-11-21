package icynote.noteproviders.interactors;


import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

import icynote.note.Note;
import icynote.noteproviders.templates.NoteTests;
import icynote.note.Response;
import icynote.note.impl.NoteData;
import icynote.note.decorators.ConstId;
import icynote.note.decorators.NullInput;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class ConstIDInteractorTests extends NoteTests {
    @Override
    protected Note<String> makeData() {
        return new NoteData();
    }

    @Override
    protected Note<String> addInteractor(Note<String> data) {
        return new NullInput<String>(new ConstId<String>(data));
    }

    @Override
    public void setId() {
        Response r = toTest.setId(0);
        assertNotNull(r);
        assertFalse(r.isPositive());
    }

}