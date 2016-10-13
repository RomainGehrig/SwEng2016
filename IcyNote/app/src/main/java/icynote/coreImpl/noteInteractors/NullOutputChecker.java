package icynote.coreImpl.noteInteractors;

import java.util.GregorianCalendar;

import icynote.core.Note;

import static icynote.coreImpl.Checker.checkNotNull;


/**
 * {@code NoteInteractor} ensuring that {@code null} is never returned from its delegate.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class NullOutputChecker extends NoteInteractor {
    public NullOutputChecker(Note delegateInteractor) {
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
    public String getContent() {
        return checkNotNull(super.getContent());
    }

    @Override
    public String getTitle() {
        return checkNotNull(super.getTitle());
    }
}
