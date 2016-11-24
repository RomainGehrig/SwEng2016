package icynote.note.decorators;

import java.util.GregorianCalendar;

import icynote.note.Note;

import static icynote.note.common.BoundaryCheckerUtil.checkNotNull;


/**
 * Note interactor ensuring that {@code null} is never returned from its delegate.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class NullOutput<S> extends NoteDecoratorTemplate<S> {
    private String name = "";

    public NullOutput(Note<S> delegateInteractor) {
        super(delegateInteractor);
    }
    public NullOutput(Note<S> delegateInteractor, String errorMessage) {
        super(delegateInteractor);
        name = errorMessage;
    }

    @Override
    public GregorianCalendar getLastUpdate() {
        return checkNotNull(super.getLastUpdate(), name);
    }

    @Override
    public GregorianCalendar getCreation() {
        return checkNotNull(super.getCreation(), name);
    }

    @Override
    public S getContent() {
        return checkNotNull(super.getContent(), name);
    }

    @Override
    public S getTitle() {
        return checkNotNull(super.getTitle(), name);
    }
}
