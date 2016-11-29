package icynote.note.decorators;

import java.util.GregorianCalendar;

import icynote.note.Note;
import icynote.note.Response;
import icynote.note.impl.NoteData;

/**
 * Delegates every method to a delegate {@code Note};
 * it is a template that should be extended by specific note interactors.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class NoteDecoratorTemplate<S> implements Note<S> {

    private final Note<S> delegate;

    //-------------------------------------

    protected NoteDecoratorTemplate(Note<S> delegateInteractor) {
        delegate = delegateInteractor;
    }

    //-------------------------------------


    @Override
    public NoteData<S> getRaw() {
        return delegate.getRaw();
    }

    @Override
    public int getId() {
        return delegate.getId();
    }

    @Override
    public S getTitle() {
        return delegate.getTitle();
    }

    @Override
    public S getContent() {
        return delegate.getContent();
    }

    @Override
    public GregorianCalendar getCreation() {
        return delegate.getCreation();
    }

    @Override
    public GregorianCalendar getLastUpdate() {
        return delegate.getLastUpdate();
    }

    @Override
    public Response setId(int newId) {
        return delegate.setId(newId);
    }

    @Override
    public Response setTitle(S newTitle) {
        return delegate.setTitle(newTitle);
    }

    @Override
    public Response setContent(S newContent) {
        return delegate.setContent(newContent);
    }

    @Override
    public Response setCreation(GregorianCalendar creationDate) {
        return delegate.setCreation(creationDate);
    }

    @Override
    public Response setLastUpdate(GregorianCalendar lastUpdateDate) {
        return delegate.setLastUpdate(lastUpdateDate);
    }
}
