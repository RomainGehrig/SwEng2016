package icynote.core.impl.interactors;

import icynote.core.Note;
import icynote.core.Response;
import icynote.core.impl.ResponseFactory;

/**
 * Note interactor that blocks the {@code setId} method.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class ConstIdInteractor extends NoteInteractorTemplate {
    public ConstIdInteractor(Note delegateInteractor) {
        super(delegateInteractor);
    }

    @Override
    public Response setId(int newId) {
        return ResponseFactory.negativeResponse();
    }
}
