package icynote.noteproviders.decorators;

import java.util.Iterator;

import icynote.noteproviders.NoteProvider;
import icynote.noteproviders.OrderBy;
import icynote.noteproviders.OrderType;
import icynote.note.Response;
import util.DelegatingIterator;
import util.Optional;

import static icynote.note.common.BoundaryCheckerUtil.checkNotNull;


/**
 * This class checks that values provided to and returned by its delegate (an other {@code NoteProvider}
 * implementation) are not {@code null}.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
@SuppressWarnings("ReturnOfInnerClass")
public class CheckedNoteProvider<N> implements NoteProvider<N> {
    private final NoteProvider<N> delegate;

    public CheckedNoteProvider(NoteProvider<N> toCheck) {
        checkNotNull(toCheck);
        delegate = toCheck;
    }

    @Override
    public Optional<N> createNote() {
        return checkNotNull(delegate.createNote());
    }

    @Override
    public Optional<N> getNote(int id) {
        return checkNotNull(delegate.getNote(id));
    }

    @Override
    public Iterable<N> getNotes(OrderBy index, OrderType order) {
        checkNotNull(index);
        checkNotNull(order);

        final Iterable<N> provided = delegate.getNotes(index, order);
        checkNotNull(provided);

        return new Iterable<N>() {
            @Override
            public Iterator<N> iterator() {
                return new DelegatingIterator<N>(provided.iterator()) {
                    @Override
                    public N next() {
                        return checkNotNull(super.next());
                    }
                };
            }
        };
    }

    @Override
    public Response persist(N n) {
        checkNotNull(n);
        return checkNotNull(delegate.persist(n));
    }

    @Override
    public Response delete(int id) {
        return checkNotNull(delegate.delete(id));
    }
}
