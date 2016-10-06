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
    private NoteData mData;
    private NoteStorageBdr mStorage;
    private boolean mHasTitleChanged = false;
    private boolean mHasContentChanged = false;
    
    public EditableNoteItr(NoteData data, NoteStorageBdr storage) {
        mData = data;
        mStorage = storage;
    }
    
    /* ---------------------------------------- */
    
    @Override
    public int getId() {
        return mData.getId();
    }
    @Override
    public Date getDateOfCreation() {
        return mData.getCreation();
    }
    @Override
    public Date getDateOfLastModification() {
        return mData.getLastModification();
    }
    @Override
    public String getTitle() {
        return mData.getTitle();
    }
    @Override
    public String getContent() {
        return mData.getContent();
    }
    
    /* ---------------------------------------- */
    
    @Override
    public boolean setTitle(String newTitle) {
        mHasTitleChanged = true;
        mData.setTitle(newTitle);
        return true;
    }
    @Override
    public boolean setContent(String newContent) {
        mHasContentChanged = true;
        mData.setContent(newContent);
        return true;
    }

    /* ---------------------------------------- */
    
    @Override
    public boolean save() {
        if(mHasTitleChanged && mHasContentChanged) {
            return mStorage.saveNote(mData);
        } else if (mHasTitleChanged) {
            return mStorage.updateTitle(mData.getId(), mData.getTitle());
        } else {
            return true;
        }
    }
    
    @Override
    public boolean delete() {
        return mStorage.deleteNote(mData.getId());
    }
}
