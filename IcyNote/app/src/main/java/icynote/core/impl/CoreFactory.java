package icynote.core.impl;


import icynote.core.Note;
import icynote.core.Storage;
import icynote.core.impl.interactors.DateInteractor;
import icynote.core.impl.interactors.NoteInteractorFactory;
import icynote.core.impl.interactors.NullInputInteractor;
import icynote.core.impl.interactors.NullOutputInteractor;

/**
 * Simple factory class with default configuration for the core.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public final class CoreFactory {
    public static icynote.core.IcyNoteCore core(Storage s) {
        IcyNoteCoreImpl core = new IcyNoteCoreImpl(s);
        core.stack(new NoteInteractorFactory() {
            @Override
            public Note make(Note delegate) {
                Note tmp = delegate;
                tmp = new NullOutputInteractor(tmp);
                tmp = new DateInteractor(tmp);
                tmp = new NullInputInteractor(tmp);
                return tmp;
            }
        });
        return core;
    }

    private CoreFactory(){}
}
