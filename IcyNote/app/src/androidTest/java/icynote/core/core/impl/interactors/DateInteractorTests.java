package icynote.core.impl.interactors;

import org.junit.Test;

import java.util.GregorianCalendar;

import icynote.core.Note;
import icynote.core.NoteTests;
import icynote.core.Response;
import icynote.core.impl.NoteData;
import icynote.core.impl.ResponseFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;


public class DateInteractorTests extends NoteTests {

    @Override
    protected Note addInteractor(Note data) {
        return new NullInputInteractor(new DateInteractor(data));
    }

    //----------------------------------------------------------------------------------


    @Override
    protected Note makeData() {
        return new NoteData();
    }

    @Override
    public void setCreation() {
        GregorianCalendar d = new GregorianCalendar();
        Response r = toTest.setCreation(d);

        assertNotNull("response is not null", r);
        assertFalse("response is negative", r.isPositive());
        assertEquals("check old value", toTest.getCreation(), originalCreation);
    }


    @Override
    public void setLastUpdate() {
        GregorianCalendar d = new GregorianCalendar();
        Response r = toTest.setLastUpdate(d);

        assertNotNull("response is not null", r);
        assertFalse("response is negative", r.isPositive());
        assertEquals("check old value", toTest.getLastUpdate(), originalUpdate);
    }

    @Override
    public void setTitle() {
        assertEquals(toTest.getLastUpdate(), originalUpdate);
        super.setTitle();
        assertNotEquals(toTest.getLastUpdate(), originalUpdate);
    }

    @Test
    public void setTitleWhenDelegateRefuses() {
        Note blocked = getBlockedNote();
        Response r = blocked.setTitle("should be refused");
        assertFalse(r.isPositive());
    }

    private Note getBlockedNote() {
        NoteData data = new NoteData();
        data.setTitle(originalTitle);
        data.setContent(originalContent);
        data.setCreation(originalCreation);
        data.setLastUpdate(originalUpdate);

        NoteInteractorTemplate blocker = new NoteInteractorTemplate(data) {
            @Override
            public Response setLastUpdate(GregorianCalendar lastUpdateDate) {
                return ResponseFactory.negativeResponse();
            }
        };
        return new DateInteractor(blocker);
    }
}
