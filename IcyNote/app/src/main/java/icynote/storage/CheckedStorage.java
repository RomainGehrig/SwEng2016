package icynote.storage;

import java.util.Iterator;

import icynote.core.Note;
import icynote.core.OrderBy;
import icynote.core.OrderType;
import icynote.core.Response;
import icynote.core.Storage;
import util.Optional;

import static icynote.coreImpl.Checker.checkNotNull;


/**
 * This class checks that values provided to and returned by its delegate (an other {@code Storage}
 * implementation) are not {@code null}.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class CheckedStorage implements Storage {
    private final Storage delegate;

    public CheckedStorage(Storage toCheck) {
        checkNotNull(toCheck);
        delegate = toCheck;
    }

    @Override
    public Optional<Note> createNote() {
        return checkNotNull(delegate.createNote());
    }

    @Override
    public Optional<Note> getNote(int id) {
        return checkNotNull(delegate.getNote(id));
    }

    @Override
    public Iterable<Note> getNotes(OrderBy index, OrderType order) {
        checkNotNull(index);
        checkNotNull(order);

        final Iterable<Note> provided = delegate.getNotes(index, order);
        checkNotNull(provided);

        return new Iterable<Note>() {
            @Override
            public Iterator<Note> iterator() {
                final Iterator<Note> it = provided.iterator();
                checkNotNull(it);

                return new Iterator<Note>() {
                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public Note next() {
                        return checkNotNull(it.next());
                    }

                    @Override
                    public void remove() {
                        it.remove();
                    }
                };
            }
        };
    }

    @Override
    public Response persist(Note n) {
        checkNotNull(n);
        return checkNotNull(delegate.persist(n));
    }

    @Override
    public Response delete(int id) {
        return checkNotNull(delegate.delete(id));
    }
}
