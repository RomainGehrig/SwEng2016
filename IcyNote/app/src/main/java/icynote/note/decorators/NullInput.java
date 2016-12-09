package icynote.note.decorators;

import java.util.GregorianCalendar;

import icynote.note.Note;
import icynote.note.Response;
import icynote.note.common.BoundaryCheckerUtil;

/**
 * Note interactor ensuring that {@code null} is never provided to its delegate.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class NullInput<S> extends NoteDecoratorTemplate<S> {
    private String name = "";

    /**
     * Instantiates a new Null input.
     *
     * @param delegateInteractor the delegate interactor
     */
    public NullInput(Note<S> delegateInteractor) {
        super(delegateInteractor);
        name = "";
    }

    /**
     * Instantiates a new Null input.
     *
     * @param delegateInteractor the delegate interactor
     * @param errorMessage       the error message
     */
    public NullInput(Note<S> delegateInteractor, String errorMessage) {
        super(delegateInteractor);
        name = errorMessage;
    }

    @Override
    public Response setTitle(S newTitle) {
        BoundaryCheckerUtil.checkNotNull(newTitle, name);
        return super.setTitle(newTitle);
    }

    @Override
    public Response setContent(S newContent) {
        BoundaryCheckerUtil.checkNotNull(newContent, name);
        return super.setContent(newContent);
    }

    @Override
    public Response setCreation(GregorianCalendar creationDate) {
        BoundaryCheckerUtil.checkNotNull(creationDate, name);
        return super.setCreation(creationDate);
    }

    @Override
    public Response setLastUpdate(GregorianCalendar lastUpdateDate) {
        BoundaryCheckerUtil.checkNotNull(lastUpdateDate, name);
        return super.setLastUpdate(lastUpdateDate);
    }
}
