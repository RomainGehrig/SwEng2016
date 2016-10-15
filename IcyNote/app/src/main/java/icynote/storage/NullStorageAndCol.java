package icynote.storage;

import icynote.core.Note;
import icynote.core.OrderBy;
import icynote.core.OrderType;
import icynote.core.Response;
import icynote.core.Storage;
import util.Optional;

/**
 * Mock Storage to be used during testing that always returns null.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class NullStorageAndCol implements Storage {
    @Override
    public Optional<Note> createNote() {
        return null;
    }

    @Override
    public Optional<Note> getNote(int id) {
        return null;
    }

    @Override
    public Iterable<Note> getNotes(OrderBy index, OrderType order) {
        return null;
    }

    @Override
    public Response persist(Note n) {
        return null;
    }

    @Override
    public Response delete(int id) {
        return null;
    }
}
