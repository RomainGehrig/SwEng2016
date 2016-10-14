package icynote.coreImpl;


import icynote.core.Note;
import icynote.core.Storage;
import icynote.coreImpl.noteInteractors.DateManager;
import icynote.coreImpl.noteInteractors.NoteInteractorFactory;
import icynote.coreImpl.noteInteractors.NullInputChecker;
import icynote.coreImpl.noteInteractors.NullOutputChecker;

/**
 * Simple factory class with default configuration for the core.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class CoreFactory {
    public static icynote.core.IcyNoteCore core(Storage s) {
        IcyNoteCore core = new IcyNoteCore(s);
        core.stack(new NoteInteractorFactory() {
            @Override
            public Note make(Note delegate) {
                Note tmp = delegate;
                tmp = new NullOutputChecker(tmp);
                tmp = new DateManager(tmp);
                tmp = new NullInputChecker(tmp);
                return tmp;
            }
        });
        return core;
    }
}
