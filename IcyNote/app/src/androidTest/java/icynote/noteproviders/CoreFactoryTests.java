package icynote.noteproviders;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.GregorianCalendar;

import icynote.note.Note;
import icynote.note.Response;
import icynote.noteproviders.impl.Factory;
import icynote.noteproviders.persistent.ListNoteProvider;
import util.Optional;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CoreFactoryTests {
    @Test(expected=AssertionError.class)
    public void createFactory(){
        Factory c = new Factory();
        assertNull(c);
    }

    @Test
    public void coreCanBeCreated() {
        NoteProvider<Note<String>> c = Factory.addNoteDecorators(new ListNoteProvider());
        assertNotNull(c);
    }

    @Test
    public void notesHaveDateValidationLogic() {
        NoteProvider<Note<String>> c = Factory.addNoteDecorators(new ListNoteProvider());

        Optional<Note<String>> n = c.createNote();
        assertTrue("note created", n.isPresent());

        Response r = n.get().setCreation(new GregorianCalendar());
        assertFalse("creation", r.isPositive());


        Response s = n.get().setLastUpdate(new GregorianCalendar());
        assertFalse("creation", s.isPositive());
    }

    @Test(expected = IllegalArgumentException.class)
    public void notesHaveNullChecks() {
        NoteProvider<Note<String>> c = Factory.addNoteDecorators(new ListNoteProvider());
        Optional<Note<String>> opt = c.createNote();
        assertTrue(opt.isPresent());
        Note<String> n = opt.get();
        n.setTitle(null);
    }
}