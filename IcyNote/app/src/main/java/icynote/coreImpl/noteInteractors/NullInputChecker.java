package icynote.coreImpl.noteInteractors;

import java.util.GregorianCalendar;

import icynote.core.Note;
import icynote.core.Response;
import icynote.coreImpl.Checker;

/**
 * {@code NoteInteractor} ensuring that {@code null} is never provided to its delegate.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class NullInputChecker extends NoteInteractor {
    public NullInputChecker(Note delegateInteractor) {
        super(delegateInteractor);
    }

    @Override
    public Response setTitle(String newTitle) {
        Checker.checkNotNull(newTitle);
        return super.setTitle(newTitle);
    }

    @Override
    public Response setContent(String newContent) {
        Checker.checkNotNull(newContent);
        return super.setContent(newContent);
    }

    @Override
    public Response setCreation(GregorianCalendar creationDate) {
        Checker.checkNotNull(creationDate);
        return super.setCreation(creationDate);
    }

    @Override
    public Response setLastUpdate(GregorianCalendar lastUpdateDate) {
        Checker.checkNotNull(lastUpdateDate);
        return super.setLastUpdate(lastUpdateDate);
    }
}
