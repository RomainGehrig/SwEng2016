package icynote.coreImpl.noteInteractors;

import java.util.GregorianCalendar;

import icynote.core.Note;
import icynote.core.Response;

/**
 * Delegates every method to a delegate {@code Note};
 * it is a template that should be extended by specific note interactors.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class NoteInteractor implements Note {

    protected final Note delegate;

    //-------------------------------------

    protected NoteInteractor(Note delegateInteractor) {
        delegate = delegateInteractor;
    }

    //-------------------------------------

    @Override
    public int getId() {
        return delegate.getId();
    }

    @Override
    public String getTitle() {
        return delegate.getTitle();
    }

    @Override
    public String getContent() {
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
    public Response setId(int id) {
        return delegate.setId(id);
    }

    @Override
    public Response setTitle(String newTitle) {
        return delegate.setTitle(newTitle);
    }

    @Override
    public Response setContent(String newContent) {
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
