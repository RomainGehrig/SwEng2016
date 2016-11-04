package icynote.core.impl;


import org.junit.Test;

import icynote.core.Response;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ResponseFactoryTests {

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
