package icynote.noteproviders.decorators;

import java.util.Iterator;

import icynote.note.Note;
import icynote.note.Response;
import icynote.note.common.BoundaryCheckerUtil;
import icynote.note.decorators.NoteDecoratorFactory;
import icynote.note.decorators.NoteDecoratorTemplate;
import icynote.noteproviders.NoteProvider;
import icynote.noteproviders.OrderBy;
import icynote.noteproviders.OrderType;
import util.DelegatingIterator;
import util.Optional;

/**
 * Stacks note interactors on top of the {@code Note} object provided by the
 * storage. (Does not check for {@code null} values. See {@code CheckedNoteProvider} for that.
 *
 * @author Julien Harbulot
 * @version 1.0
 * @see NoteDecoratorTemplate
 */
@SuppressWarnings("ReturnOfInnerClass")
public class NoteWithInteractorsProvider<S> implements NoteProvider<Note<S>> {
    private final NoteProvider<Note<S>> storage;
    private NoteDecoratorFactory<S> wrapper = new NoteDecoratorFactory<S>();

    //--------------------------------------------------------------

    public NoteWithInteractorsProvider(NoteProvider<Note<S>> s) {
        storage = s;
    }

    //--------------------------------------------------------------

    public void stack(NoteDecoratorFactory<S> interactorFactory) {
        BoundaryCheckerUtil.checkNotNull(interactorFactory);
        wrapper = wrapper.andThen(interactorFactory);
    }

    //--------------------------------------------------------------

    @Override
    public Optional<Note<S>> createNote() {
        return addInteractor(storage.createNote());
    }

    @Override
    public Optional<Note<S>> getNote(int id) {
        return addInteractor(storage.getNote(id));
    }

    @Override
    public Iterable<Note<S>> getNotes(OrderBy index, OrderType order) {
        BoundaryCheckerUtil.checkNotNull(index);
        BoundaryCheckerUtil.checkNotNull(order);

        final Iterable<Note<S>> delegate = storage.getNotes(index, order);

        return new Iterable<Note<S>>() {
            @Override
            public Iterator<Note<S>> iterator() {
                return new DelegatingIterator<Note<S>>(delegate.iterator()) {
                    @Override
                    public Note<S> next() {
                        return wrapper.make(super.next());
                    }
                };
            }
        };
    }

    @Override
    public Response persist(Note<S> n) {
        return storage.persist(n);
    }

    @Override
    public Response delete(int id) {
        return storage.delete(id);
    }

    //--------------------------------------------------------------

    private Optional<Note<S>> addInteractor(Optional<Note<S>> note) {
        if (note.isPresent()) {
            return Optional.of(wrapper.make(note.get()));
        } else {
            return Optional.empty();
        }
    }
}
