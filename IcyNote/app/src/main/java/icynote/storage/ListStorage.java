package icynote.storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import icynote.core.Note;
import icynote.core.OrderBy;
import icynote.core.OrderType;
import icynote.core.Response;
import icynote.core.Storage;
import icynote.coreImpl.NoteData;
import icynote.coreImpl.ResponseFactory;
import icynote.coreImpl.noteInteractors.ConstID;
import util.Optional;

/**
 * Concrete Storage implementation to be used by the IcyNote core.
 * <p>
 * <p>
 * Internally, this class uses an ArrayList and ensures uniqueness of notes' IDs.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class ListStorage implements Storage {
    private final ArrayList<Note> notes = new ArrayList<>();
    private int nextId = 0;

    public ListStorage() {
    }

    public ListStorage(List<Note> memory) {
        for (Note n : memory) {
            notes.add(new NoteData(n));
        }
    }

    @Override
    public Optional<Note> createNote() {

        Note created = new NoteData();
        created.setId(nextId);
        ++nextId;
        notes.add(created);

        Note validator = new ConstID(created);

        return Optional.of(validator);
    }

    @Override
    public Optional<Note> getNote(int id) {
        for (Note note : notes) {
            if (note.getId() == id) {
                Note defensiveCopy = new NoteData(note);
                return Optional.of((Note) new ConstID(defensiveCopy));
            }
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Note> getNotes(final OrderBy index, final OrderType order) {
        ArrayList<Note> unsorted = new ArrayList<>();
        for (Note n : notes) {
            Note defensiveCopy = new NoteData(n);
            unsorted.add(new ConstID(defensiveCopy));
        }
        Collections.sort(unsorted, new Comparator<Note>() {
            @Override
            public int compare(Note o1, Note o2) {
                Note lhs = o1;
                Note rhs = o2;

                if (order == OrderType.DSC) {
                    lhs = o2;
                    rhs = o1;
                }
                switch (index) {
                    case TITLE:
                        return lhs.getTitle().compareTo(rhs.getTitle());
                    case CREATION:
                        return lhs.getCreation().compareTo(rhs.getCreation());
                    case LAST_UPDATE:
                        return lhs.getLastUpdate().compareTo(rhs.getLastUpdate());
                    default:
                        throw new AssertionError(index);
                }
            }
        });
        return unsorted;
    }


    @Override
    public Response persist(Note n) {
        for (int i = 0; i < notes.size(); ++i) {
            if (notes.get(i).getId() == n.getId()) {
                Note defensiveCopy = new NoteData(n);
                notes.set(i, defensiveCopy);
                return ResponseFactory.positiveResponse();
            }
        }
        return ResponseFactory.negativeResponse();
    }

    @Override
    public Response delete(int id) {
        for (Iterator<Note> it = notes.iterator(); it.hasNext(); ) {
            if (it.next().getId() == id) {
                it.remove();
                return ResponseFactory.positiveResponse();
            }
        }
        return ResponseFactory.negativeResponse();
    }
}
