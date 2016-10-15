package icynote.core;

/**
 * Primary interface of the core.
 *
 * @author Julien Harbulot
 * @version 1.0
 * @see icynote.core.NoteProvider
 */
@SuppressWarnings("MarkerInterface")
public interface IcyNoteCore extends NoteProvider {
    //this is empty for now, but we might want to change the Core interface without changing the Storage
    //interface.
    //Better have two marker interfaces than having to change user code
}
