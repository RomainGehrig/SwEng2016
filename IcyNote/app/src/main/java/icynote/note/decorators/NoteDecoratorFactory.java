package icynote.note.decorators;

import icynote.note.Note;

/**
 * Template factory to create a note interactor; should be subclassed for each specific
 * note interactor implementations.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class NoteDecoratorFactory<S> {
    public Note<S> make(Note<S> delegate) { //you should override this method
        return delegate;
    }

    public NoteDecoratorFactory<S> andThen(final NoteDecoratorFactory<S> other){
        return NoteDecoratorFactory.merge(this, other);
    }

    private static <S> NoteDecoratorFactory<S> merge(final NoteDecoratorFactory<S> first,
                                                     final NoteDecoratorFactory<S> second) {
        return new NoteDecoratorFactory<S>(){
            @Override
            public Note<S> make(Note<S> delegate) {
                return second.make(first.make(delegate));
            }
        };
    }
}
