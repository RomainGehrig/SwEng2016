package icynote.coreImpl;


import org.junit.Test;

import icynote.core.Response;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ResponseFactoryTests {
    @Test
    public void factoryExists() {
        new ResponseFactory();
    }

    @Test
    public void ItCanCreateAPositiveResponse() {
        Response r = ResponseFactory.positiveResponse();
        assertTrue(r.isPositive());
    }

    @Test
    public void ItCanCreateANegativeResponse() {
        Response r = ResponseFactory.negativeResponse();
        assertFalse(r.isPositive());
    }
}
