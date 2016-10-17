package icynote.core.impl;

import icynote.core.IcyNoteCore;
import icynote.core.Storage;
import icynote.core.WhenCoreHasAFaultyStorage;

public class IcyNoteCoreWithFaultyTests extends WhenCoreHasAFaultyStorage {
    @Override
    protected IcyNoteCore makeNew(Storage s) {
        return new IcyNoteCoreImpl(s);
    }
}
