package icynote.coreImpl;

import icynote.core.CoreWithFaultyStorage;
import icynote.core.IcyNoteCore;
import icynote.core.Storage;

public class IcyNoteCoreWithFaultyTests extends CoreWithFaultyStorage {
    @Override
    protected IcyNoteCore makeNew(Storage s) {
        return new icynote.coreImpl.IcyNoteCore(s);
    }
}
