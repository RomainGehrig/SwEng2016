package icynote.core;

import java.util.List;

/**
 * Interactor
 */
public class NoteProviderItr implements NoteProviderBdr{
    private NoteStorageBdr mStorage;
    
    /* ---------------------------------------- */
    
    public NoteProviderItr(NoteStorageBdr storage) {
        mStorage = storage;
    }

    /* ---------------------------------------- */
    
    @Override
    public List<NotePreviewData> previewAllNotes() {
        return mStorage.previewAllNotes();
    }
    
    @Override
    public Optional<EditableNoteBdr> getEditableNote(int id) {
        Optional<NoteData> data = mStorage.getNote(id);
        if (data.isPresent()) {
            EditableNoteBdr bdr = new EditableNoteItr(data.get(), mStorage);
            return Optional.of(bdr);
        } else {
            return Optional.empty();
        }
    }
}
