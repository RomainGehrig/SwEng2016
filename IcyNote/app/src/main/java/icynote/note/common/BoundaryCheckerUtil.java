package icynote.note.common;

import android.support.annotation.VisibleForTesting;

/**
 * Used by the addNoteDecorators to check input at the boundaries.
 * This class factors the types of the exceptions.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
public final class BoundaryCheckerUtil {
    public static <T> T checkNotNull(T obj) {
        if (obj == null) {
            throw new IllegalArgumentException("value can't be null");
        }
        return obj;
    }
    public static <T> T checkNotNull(T obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException("value can't be null! " + message);
        }
        return obj;
    }

    /**
     * Always throws an {@code AssertionError}.
     * It exists for coverage purposes only.
     * @throws AssertionError always
     */
    @VisibleForTesting
    public BoundaryCheckerUtil() {
        throw new AssertionError("LoginManagerFactory is an utility class");
    }
}
