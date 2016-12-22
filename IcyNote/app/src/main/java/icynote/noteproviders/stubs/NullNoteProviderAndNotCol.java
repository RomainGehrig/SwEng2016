package icynote.noteproviders.stubs;

import java.util.ArrayList;
import java.util.Collection;

import icynote.note.Note;
import icynote.noteproviders.OrderBy;
import icynote.noteproviders.OrderType;

/**
 * Mock NoteProvider to be used during testing that always returns either null
 * or a collection containing only null objects.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class NullNoteProviderAndNotCol extends NullNoteProviderAndCol {
    @Override

    public Iterable<Note<String>> getNotes(OrderBy index, OrderType order) {
        Collection<Note<String>> faultyList = new ArrayList<>();
        faultyList.add(null);
        faultyList.add(null);
        faultyList.add(null);
        return faultyList;
    }
}
