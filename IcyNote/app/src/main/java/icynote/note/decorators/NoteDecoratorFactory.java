package icynote.note.decorators;

import icynote.note.Note;

/**
 * Template factory to create a note interactor; should be subclassed for each specific
 * note interactor implementations.
 *
 * @param <S> the type parameter
 * @author Julien Harbulot
 * @version 1.0
 */
public class NoteDecoratorFactory<S> {
    /**
     * Make note.
     *
     * @param delegate the delegate
     * @return the note
     */
    public Note<S> make(Note<S> delegate) { //you should override this method
        return delegate;
    }

    /**
     * And then note decorator factory.
     *
     * @param other the other
     * @return the note decorator factory
     */
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
