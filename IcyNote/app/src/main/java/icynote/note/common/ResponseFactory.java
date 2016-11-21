package icynote.note.common;

import android.support.annotation.VisibleForTesting;

import icynote.note.Response;

/**
 * Provides an interface to create a positive or negative Response object.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
@SuppressWarnings({"ReturnOfInnerClass", "UtilityClassWithoutPrivateConstructor"})
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

    /**
     * Always throws an {@code AssertionError}.
     * It exists for coverage purposes only.
     * @throws AssertionError always
     */
    @VisibleForTesting
    public ResponseFactory() {
        throw new AssertionError("LoginManagerFactory is an utility class");
    }

}
