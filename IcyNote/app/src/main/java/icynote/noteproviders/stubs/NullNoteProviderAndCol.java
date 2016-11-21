package icynote.noteproviders.stubs;

import icynote.note.Note;
import icynote.noteproviders.OrderBy;
import icynote.noteproviders.OrderType;
import icynote.note.Response;
import icynote.noteproviders.NoteProvider;
import util.Optional;

/**
 * Mock NoteProvider to be used during testing that always returns null.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class NullNoteProviderAndCol implements NoteProvider<Note<String>> {
    @Override
    public Optional<Note<String>> createNote() {
        return null;
    }

    @Override
    public Optional<Note<String>> getNote(int id) {
        return null;
    }

    @Override
    public Iterable<Note<String>> getNotes(OrderBy index, OrderType order) {
        return null;
    }

    @Override
    public Response persist(Note<String> n) {
        return null;
    }

    @Override
    public Response delete(int id) {
        return null;
    }
}
