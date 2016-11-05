package icynote.core.impl;

import java.util.Iterator;

import icynote.core.Note;
import icynote.core.OrderBy;
import icynote.core.OrderType;
import icynote.core.Response;
import icynote.core.Storage;
import icynote.core.impl.interactors.NoteInteractorFactory;
import icynote.core.impl.interactors.NoteInteractorTemplate;
import icynote.storage.CheckedStorage;
import util.DelegatingIterator;
import util.Optional;

/**
 * Stacks note interactors on top of the {@code Note} object provided by the
 * storage. The class also performs input validation by testing for {@code null} values.
 *
 * @author Julien Harbulot
 * @version 1.0
 * @see NoteInteractorTemplate
 */
@SuppressWarnings("ReturnOfInnerClass")
class IcyNoteCoreImpl implements icynote.core.IcyNoteCore {
    private final CheckedStorage storage;
    private NoteInteractorFactory wrapper = new NoteInteractorFactory();

    //--------------------------------------------------------------

    public IcyNoteCoreImpl(Storage s) {
        storage = new CheckedStorage(s);
    }

    //--------------------------------------------------------------

    public void stack(NoteInteractorFactory interactorFactory) {
        BoundaryCheckerUtil.checkNotNull(interactorFactory);
        wrapper = wrapper.andThen(interactorFactory);
    }

    //--------------------------------------------------------------

    @Override
    public Optional<Note> createNote() {
        return addInteractor(storage.createNote());
    }

    @Override
    public Optional<Note> getNote(int id) {
        return addInteractor(storage.getNote(id));
    }

    @Override
    public Iterable<Note> getNotes(OrderBy index, OrderType order) {
        BoundaryCheckerUtil.checkNotNull(index);
        BoundaryCheckerUtil.checkNotNull(order);

        final Iterable<Note> delegate = storage.getNotes(index, order);

        return new Iterable<Note>() {
            @Override
            public Iterator<Note> iterator() {
                return new DelegatingIterator<Note>(delegate.iterator()) {
                    @Override
                    public Note next() {
                        return wrapper.make(super.next());
                    }
                };
            }
        };
    }

    @Override
    public Response persist(Note n) {
        return storage.persist(n);
    }

    @Override
    public Response delete(int id) {
        return storage.delete(id);
    }

    //--------------------------------------------------------------

    private Optional<Note> addInteractor(Optional<Note> note) {
        if (note.isPresent()) {
            return Optional.of(wrapper.make(note.get()));
        } else {
            return Optional.empty();
        }
    }
}
