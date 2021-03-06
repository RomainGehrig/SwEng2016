package icynote.note.decorators;

import icynote.note.Note;
import icynote.note.Response;
import icynote.note.common.ResponseFactory;

/**
 * Note interactor that blocks the {@code setId} method.
 *
 * @param <S> the type parameter
 * @author Julien Harbulot
 * @version 1.0
 */
public class ConstId<S> extends NoteDecoratorTemplate<S> {
    /**
     * Instantiates a new Const id.
     *
     * @param delegateInteractor the delegate interactor
     */
    public ConstId(Note<S> delegateInteractor) {
        super(delegateInteractor);
    }

    @Override
    public Response setId(int newId) {
        return ResponseFactory.negativeResponse();
    }
}
