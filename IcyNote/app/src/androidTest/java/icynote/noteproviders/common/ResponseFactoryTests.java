package icynote.noteproviders.common;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import icynote.note.Response;
import icynote.note.common.ResponseFactory;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class ResponseFactoryTests {

    @Test(expected=AssertionError.class)
    public void createFactory(){
        ResponseFactory c = new ResponseFactory();
        assertNull(c);
    }
    @Test
    public void itCanCreateAPositiveResponse() {
        Response r = ResponseFactory.positiveResponse();
        assertTrue(r.isPositive());
    }

    @Test
    public void itCanCreateANegativeResponse() {
        Response r = ResponseFactory.negativeResponse();
        assertFalse(r.isPositive());
    }
}
