package icynote.core.impl;

import org.junit.Test;

import java.util.GregorianCalendar;

import icynote.core.Note;
import icynote.core.Response;
import icynote.storage.ListStorage;
import util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CoreFactoryTests {
    @Test
    public void coreCanBeCreated() {
        icynote.core.IcyNoteCore c = CoreFactory.core(new ListStorage());
        assertNotNull(c);
    }

    @Test
    public void notesHaveDateValidationLogic() {
        icynote.core.IcyNoteCore c = CoreFactory.core(new ListStorage());

        Optional<Note> n = c.createNote();
        assertTrue("note created", n.isPresent());

        Response r = n.get().setCreation(new GregorianCalendar());
        assertFalse("creation", r.isPositive());


        Response s = n.get().setLastUpdate(new GregorianCalendar());
        assertFalse("creation", s.isPositive());
    }

    @Test(expected = IllegalArgumentException.class)
    public void notesHaveNullChecks() {
        icynote.core.IcyNoteCore c = CoreFactory.core(new ListStorage());
        Optional<Note> opt = c.createNote();
        assertTrue(opt.isPresent());
        Note n = opt.get();
        n.setTitle(null);
    }
}