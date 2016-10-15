package icynote.core.impl;

import icynote.core.Response;

/**
 * Provides an interface to create a positive or negative Response object.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
@SuppressWarnings("ReturnOfInnerClass")
public final class ResponseFactory {
    public static Response positiveResponse() {
        return new Response() {
            @Override
            public boolean isPositive() {
                return true;
            }
        };
    }

    public static Response negativeResponse() {
        return new Response() {
            @Override
            public boolean isPositive() {
                return false;
            }
        };
    }

    private ResponseFactory(){}
}
