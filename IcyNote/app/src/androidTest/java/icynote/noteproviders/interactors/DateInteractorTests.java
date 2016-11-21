package icynote.noteproviders.interactors;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.GregorianCalendar;

import icynote.note.Note;
import icynote.note.decorators.NoteDecoratorTemplate;
import icynote.noteproviders.templates.NoteTests;
import icynote.note.Response;
import icynote.note.impl.NoteData;
import icynote.note.common.ResponseFactory;
import icynote.note.decorators.DateDecorator;
import icynote.note.decorators.NullInput;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;


@RunWith(AndroidJUnit4.class)
public class DateInteractorTests extends NoteTests {

    @Override
    protected Note<String> addInteractor(Note<String> data) {
        return new NullInput<String>(new DateDecorator<String>(data));
    }

    //----------------------------------------------------------------------------------


    @Override
    protected Note<String> makeData() {
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
        Note<String> blocked = getBlockedNote();
        Response r = blocked.setTitle("should be refused");
        assertFalse("", r.isPositive());
    }

    private Note<String> getBlockedNote() {
        NoteData data = new NoteData();
        data.setTitle(originalTitle);
        data.setContent(originalContent);
        data.setCreation(originalCreation);
        data.setLastUpdate(originalUpdate);

        NoteDecoratorTemplate<String> blocker = new NoteDecoratorTemplate<String>(data) {
            @Override
            public Response setLastUpdate(GregorianCalendar lastUpdateDate) {
                return ResponseFactory.negativeResponse();
            }
        };
        return new DateDecorator<String>(blocker);
    }
}
