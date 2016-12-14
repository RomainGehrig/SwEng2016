package icynote.note.decorators;

import java.util.GregorianCalendar;

import icynote.note.Note;
import icynote.note.Response;
import icynote.note.common.ResponseFactory;


/**
 * Note interactor that updates the {@code lastUpdate}
 * date each time a field of the wrapped {@code Note} changes; it also deactivates the setters
 * {@code setCreation} and {@code setLastUpdate} to preserve
 * integrity of data.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class DateDecorator<S> extends NoteDecoratorTemplate<S> {

    /**
     * Instantiates a new Date decorator.
     *
     * @param delegateInteractor the delegate interactor
     */
    public DateDecorator(Note<S> delegateInteractor) {
        super(delegateInteractor);
    }

    //------------------------------------------------------------------------------

    @Override
    public Response setId(int newId) {
        return updateIfNeeded(super.setId(newId));

    }

    @Override
    public Response setTitle(S newTitle) {
        return updateIfNeeded(super.setTitle(newTitle));
    }

    @Override
    public Response setContent(S newContent) {
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
            Response updateResp = super.setLastUpdate(new GregorianCalendar());
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
