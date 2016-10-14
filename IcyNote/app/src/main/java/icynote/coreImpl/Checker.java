package icynote.coreImpl;

/**
 * Used by the core to check input at the boundaries.
 * This class factors the types of the exceptions.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class Checker {
    public static <T> T checkNotNull(T obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
        return obj;
    }
}
