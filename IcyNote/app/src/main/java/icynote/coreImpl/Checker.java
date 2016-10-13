package icynote.coreImpl;

/**
 * Provides method(s) to check whether an argument is {@code null}
 * and to throw the appropriate {@code Exception}.
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
