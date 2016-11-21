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
    public NullOutput(Note<S> delegateInteractor) {
        super(delegateInteractor);
    }

    @Override
    public GregorianCalendar getLastUpdate() {
        return checkNotNull(super.getLastUpdate());
    }

    @Override
    public GregorianCalendar getCreation() {
        return checkNotNull(super.getCreation());
    }

    @Override
    public S getContent() {
        return checkNotNull(super.getContent());
    }

    @Override
    public S getTitle() {
        return checkNotNull(super.getTitle());
    }
}
