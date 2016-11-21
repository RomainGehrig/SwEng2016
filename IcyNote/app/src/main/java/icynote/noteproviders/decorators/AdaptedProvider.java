package icynote.noteproviders.decorators;

import java.util.Iterator;

import icynote.note.Response;
import icynote.noteproviders.NoteProvider;
import icynote.noteproviders.OrderBy;
import icynote.noteproviders.OrderType;
import util.Adapter;
import util.DelegatingIterator2;
import util.Optional;


public class AdaptedProvider<S, T> implements NoteProvider<S> {
    private NoteProvider<T> adapted;
    private Adapter<S,T> adapter;

    public AdaptedProvider(NoteProvider<T> toAdapt, Adapter<S, T> usingAdapter) {
        adapted = toAdapt;
        adapter = usingAdapter;
    }

    @Override
    public Optional<S> createNote() {
        return adapt(adapted.createNote());
    }

    @Override
    public Optional<S> getNote(int id) {
        return adapt(adapted.getNote(id));
    }

    @Override
    public Iterable<S> getNotes(OrderBy index, OrderType order) {
        final Iterable<T> delegate = adapted.getNotes(index, order);

        return new Iterable<S>() {
            @Override
            public Iterator<S> iterator() {
                return new DelegatingIterator2<T, S>(delegate.iterator()) {
                    @Override
                    public S next() {
                        return adapter.to(getDelegate().next());
                    }
                };
            }
        };
    }

    @Override
    public Response persist(S s) {
        return adapted.persist(adapter.from(s));
    }

    @Override
    public Response delete(int id) {
        return adapted.delete(id);
    }

    private Optional<S> adapt(Optional<T> note) {
        if (note.isPresent()) {
            return Optional.of(adapter.to(note.get()));
        } else {
            return Optional.empty();
        }
    }
}
