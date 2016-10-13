package icynote.coreImpl.noteInteractors;

import java.util.GregorianCalendar;

import icynote.core.Note;
import icynote.core.Response;
import icynote.coreImpl.ResponseFactory;


/**
 * {@code NoteInteractor} that updates the {@code lastUpdate}
 * date each time a field of the wrapped {@code Note} changes; it also deactivates the setters
 * {@code setCreation} and {@code setLastUpdate} to preserve
 * integrity of data.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class DateManager extends NoteInteractor {

    public DateManager(Note delegateInteractor) {
        super(delegateInteractor);
    }

    //------------------------------------------------------------------------------

    @Override
    public Response setId(int id) {
        return updateIfNeeded(super.setId(id));

    }

    @Override
    public Response setTitle(String newTitle) {
        return updateIfNeeded(super.setTitle(newTitle));
    }

    @Override
    public Response setContent(String newContent) {
        return updateIfNeeded(super.setContent(newContent));
    }

    @Override
    public Response setCreation(GregorianCalendar creationDate) {
        return negativeResponse();
    }

    @Override
    public Response setLastUpdate(GregorianCalendar lastUpdateDate) {
        return negativeResponse();
    }

    //------------------------------------------------------------------------------

    private Response updateIfNeeded(Response delegateResponse) {
        if (delegateResponse.isPositive()) {
            Response updateResp = delegate.setLastUpdate(new GregorianCalendar());
            if (!updateResp.isPositive()) {
                //todo create custom response
                return negativeResponse();
            }
        }
        return delegateResponse;
    }

    private Response negativeResponse() {
        return ResponseFactory.negativeResponse();
    }
}
