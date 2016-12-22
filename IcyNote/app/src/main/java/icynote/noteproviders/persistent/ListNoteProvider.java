package icynote.noteproviders.persistent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import icynote.note.Note;
import icynote.noteproviders.OrderBy;
import icynote.noteproviders.OrderType;
import icynote.note.Response;
import icynote.noteproviders.NoteProvider;
import icynote.note.impl.NoteData;
import icynote.note.common.ResponseFactory;
import icynote.note.decorators.ConstId;
import util.Optional;

/**
 * Concrete NoteProvider implementation to be used by the IcyNote addNoteDecorators.
 * <p>
 * <p>
 * Internally, this class uses an ArrayList and ensures uniqueness of notes' IDs.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class ListNoteProvider implements NoteProvider<Note<String>> {
    private final List<Note<String>> notes = new ArrayList<>();
    private int nextId = 0;

    /**
     * Instantiates a new List note provider.
     */
    public ListNoteProvider() {
    }

    /**
     * Instantiates a new List note provider.
     *
     * @param memory the memory
     */
    public ListNoteProvider(Iterable<Note<String>> memory) {
        for (Note<String> n : memory) {
            notes.add(new NoteData<>(n));
        }
    }

    @Override
    public Optional<Note<String>> createNote() {

        Note<String> created = new NoteData<>("", "");
        created.setId(nextId);

        ++nextId;
        Note<String> defensiveCopy = new NoteData<>(created);
        notes.add(defensiveCopy);

        Note<String> validator = new ConstId<>(created);

        return Optional.of(validator);
    }

    @Override
    public Optional<Note<String>> getNote(int id) {
        for (Note<String> note : notes) {
            if (note.getId() == id) {
                Note<String> defensiveCopy = new NoteData<>(note);
                return Optional.of((Note<String>) new ConstId<>(defensiveCopy));
            }
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Note<String>> getNotes(final OrderBy index, final OrderType order) {
        List<Note<String>> unsorted = new ArrayList<>();
        for (Note<String> n : notes) {
            Note<String> defensiveCopy = new NoteData<>(n);
            unsorted.add(new ConstId<>(defensiveCopy));
        }
        Collections.sort(unsorted, new Comparator<Note<String>>() {
            @Override
            public int compare(Note<String> o1, Note<String> o2) {
                Note<String> lhs = o1;
                Note<String> rhs = o2;

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
    public Response persist(Note<String> n) {
        n = n.getRaw(); //unwind the decorator stack, if there is one

        for (int i = 0; i < notes.size(); ++i) {
            if (notes.get(i).getId() == n.getId()) {
                Note<String> defensiveCopy = new NoteData<>(n);
                notes.set(i, defensiveCopy);
                return ResponseFactory.positiveResponse();
            }
        }
        return ResponseFactory.negativeResponse();
    }

    @Override
    public Response delete(int id) {
        for (Iterator<Note<String>> it = notes.iterator(); it.hasNext(); ) {
            if (it.next().getId() == id) {
                it.remove();
                return ResponseFactory.positiveResponse();
            }
        }
        return ResponseFactory.negativeResponse();
    }
}
