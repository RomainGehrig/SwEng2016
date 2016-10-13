package icynote.coreImpl.noteInteractors;

import icynote.core.Note;
import icynote.core.Response;
import icynote.coreImpl.ResponseFactory;

/**
 * {@code NoteInteractor} that blocks the {@code setId} method.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class ConstID extends NoteInteractor {
    public ConstID(Note delegateInteractor) {
        super(delegateInteractor);
    }

    @Override
    public Response setId(int id) {
        return ResponseFactory.negativeResponse();
    }
}
