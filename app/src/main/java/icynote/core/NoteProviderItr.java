package icynote.core;

import java.util.List;

/**
 * Interactor
 */
public class NoteProviderItr implements NoteProviderBdr{
    private NoteStorageBdr m_storage;
    
    /* ---------------------------------------- */
    
    public NoteProviderItr(NoteStorageBdr storage) {
        this.m_storage = storage;
    }
    /* ---------------------------------------- */
    
    @Override
    public List<NotePreviewData> previewAllNotes() {
        return m_storage.previewAllNotes();
    }
    
    @Override
    public Optional<EditableNoteBdr> getEditableNote(int id) {
        Optional<NoteData> data = m_storage.getNote(id);
        if (data.isPresent()) {
            EditableNoteBdr bdr = new EditableNoteItr(data.get(), m_storage);
            return Optional.of(bdr);
        } else {
            return Optional.empty();
        }
    }
}
