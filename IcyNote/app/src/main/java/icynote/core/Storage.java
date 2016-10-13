package icynote.core;

/**
 * Used by the core application to store and retrieve notes.
 * <p>
 * This interface may be implemented by any specific storage implementation,
 * such as a database, a filesystem or any other implementations.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
@SuppressWarnings("MarkerInterface")
public interface Storage extends NoteProvider {
}
