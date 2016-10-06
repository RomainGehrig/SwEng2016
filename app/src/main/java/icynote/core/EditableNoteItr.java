package icynote.core;

import java.util.Date;

/**
 * Interactor
 * - records editions
 * - when save() is called, the interactor calls the 
 * storage's most adapted method depending on which 
 * part of the note were edited.
 */
public class EditableNoteItr implements EditableNoteBdr {
    private NoteData m_data;
    private NoteStorageBdr m_storage;
    private boolean m_hasTitleChanged = false;
    private boolean m_hasContentChanged = false;
    
    public EditableNoteItr(NoteData data, NoteStorageBdr storage) {
        m_data = data;
        m_storage = storage;
    }
    
    /* ---------------------------------------- */
    
    @Override
    public int getId() {
        return m_data.getId();
    }
    @Override
    public Date getDateOfCreation() {
        return m_data.getCreation();
    }
    @Override
    public Date getDateOfLastModification() {
        return m_data.getLastModification();
    }
    @Override
    public String getTitle() {
        return m_data.getTitle();
    }
    @Override
    public String getContent() {
        return m_data.getContent();
    }
    
    /* ---------------------------------------- */
    
    @Override
    public boolean setTitle(String newTitle) {
        m_hasTitleChanged = true;
        m_data.setTitle(newTitle);
        return true;
    }
    @Override
    public boolean setContent(String newContent) {
        m_hasContentChanged = true;
        m_data.setContent(newContent);
        return true;
    }

    /* ---------------------------------------- */
    
    @Override
    public boolean save() {
        if(m_hasTitleChanged && m_hasContentChanged) {
            return m_storage.saveNote(m_data);
        } else if (m_hasTitleChanged) {
            return m_storage.updateTitle(m_data.getId(), m_data.getTitle());
        } else {
            return true;
        }
    }
    
    @Override
    public boolean delete() {
        return m_storage.deleteNote(m_data.getId());
    }
}
