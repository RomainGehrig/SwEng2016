package icynote.coreImpl.noteInteractors;

import icynote.core.Note;

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
