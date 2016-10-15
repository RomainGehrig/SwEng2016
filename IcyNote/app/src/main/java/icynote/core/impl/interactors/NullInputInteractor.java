package icynote.core.impl.interactors;

import java.util.GregorianCalendar;

import icynote.core.Note;
import icynote.core.Response;
import icynote.core.impl.BoundaryCheckerUtil;

/**
 * Note interactor ensuring that {@code null} is never provided to its delegate.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class NullInputInteractor extends NoteInteractorTemplate {
    public NullInputInteractor(Note delegateInteractor) {
        super(delegateInteractor);
    }

    @Override
    public Response setTitle(String newTitle) {
        BoundaryCheckerUtil.checkNotNull(newTitle);
        return super.setTitle(newTitle);
    }

    @Override
    public Response setContent(String newContent) {
        BoundaryCheckerUtil.checkNotNull(newContent);
        return super.setContent(newContent);
    }

    @Override
    public Response setCreation(GregorianCalendar creationDate) {
        BoundaryCheckerUtil.checkNotNull(creationDate);
        return super.setCreation(creationDate);
    }

    @Override
    public Response setLastUpdate(GregorianCalendar lastUpdateDate) {
        BoundaryCheckerUtil.checkNotNull(lastUpdateDate);
        return super.setLastUpdate(lastUpdateDate);
    }
}
