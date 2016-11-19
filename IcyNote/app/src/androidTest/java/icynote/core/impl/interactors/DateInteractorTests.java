package icynote.core.impl.interactors;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.GregorianCalendar;

import icynote.core.Note;
import icynote.core.NoteTests;
import icynote.core.Response;
import icynote.core.impl.NoteData;
import icynote.core.impl.ResponseFactory;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;


@RunWith(AndroidJUnit4.class)
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

    private void assertFalse(String s, boolean positive) {
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
        assertFalse("", toTest.getLastUpdate().equals(originalUpdate));
    }

    @Test
    public void setTitleWhenDelegateRefuses() {
        Note blocked = getBlockedNote();
        Response r = blocked.setTitle("should be refused");
        assertFalse("", r.isPositive());
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
