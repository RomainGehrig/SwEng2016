package icynote.storage;

import java.util.ArrayList;
import java.util.Collection;

import icynote.core.Note;
import icynote.core.OrderBy;
import icynote.core.OrderType;

/**
 * Mock Storage to be used during testing that always returns either null
 * or a collection containing only null objects.
 *
 * @author Julien Harbulot
 * @version 1.0
 */

public class NullStorageAndNotCol extends NullStorageAndCol {
    @Override

    public Iterable<Note> getNotes(OrderBy index, OrderType order) {
        Collection<Note> faultyList = new ArrayList<>();
        faultyList.add(null);
        faultyList.add(null);
        faultyList.add(null);
        return faultyList;
    }
}
