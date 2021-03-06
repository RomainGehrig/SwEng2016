package icynote.note;

/**
 * Indicates whether an operation on a Note was successful.
 *
 * @author Julien Harbulot
 * @version 1.0
 * @see Note
 */
public interface Response {
    /**
     * Returns true if and only if the operation was successfully executed.
     */
    boolean isPositive();
}
