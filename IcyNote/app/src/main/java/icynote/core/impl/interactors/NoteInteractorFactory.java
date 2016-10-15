package icynote.core.impl.interactors;

import icynote.core.Note;

/**
 * Template factory to create a note interactor; should be subclassed for each specific
 * note interactor implementations.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class NoteInteractorFactory {
    public Note make(Note delegate) { //you should override this method
        return delegate;
    }

    public NoteInteractorFactory andThen(final NoteInteractorFactory other){
        return NoteInteractorFactory.merge(this, other);
    }

    private static NoteInteractorFactory merge(final NoteInteractorFactory first,
                         final NoteInteractorFactory second) {
        return new NoteInteractorFactory(){
            @Override
            public Note make(Note delegate) {
                return second.make(first.make(delegate));
            }
        };
    }
}
