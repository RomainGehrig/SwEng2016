package icynote.core;

import java.util.Date;

import icynote.core.NoteStorageBdr.DeleteNoteResponse;
import icynote.core.NoteStorageBdr.SaveNoteResponse;
import icynote.core.NoteStorageBdr.UpdateTitleResponse;

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
    public SetTitleResponse setTitle(String newTitle) {
        mHasTitleChanged = true;
        mData.setTitle(newTitle);
        return SetTitleResponse.OK;
    }
    @Override
    public SetContentResponse setContent(String newContent) {
        mHasContentChanged = true;
        mData.setContent(newContent);
        return SetContentResponse.OK;
    }

    /* ---------------------------------------- */
    
    @Override
    public SaveResponse save() {
        if(mHasTitleChanged && mHasContentChanged) {
            SaveNoteResponse r = mStorage.saveNote(mData);
            return translateStorageResp(r);
        } else if (mHasTitleChanged) {
            UpdateTitleResponse r = mStorage.updateTitle(mData.getId(), mData.getTitle());
            return translateStorageResp(r);
        } else {
            return SaveResponse.OK;
        }
    }
    
    @Override
    public DeleteResponse delete() {
        DeleteNoteResponse r = mStorage.deleteNote(mData.getId());
        return translateStorageResp(r);
    }

    /* ---------------------------------------- */

    private SaveResponse translateStorageResp(SaveNoteResponse resp) {
        switch (resp) {
            case OK:
                return SaveResponse.OK;
            case REFUSED:
                return SaveResponse.REFUSED;
        }
        return SaveResponse.REFUSED;
    }

    private SaveResponse translateStorageResp(UpdateTitleResponse resp) {
        switch (resp) {
            case OK:
                return SaveResponse.OK;
            case REFUSED:
                return SaveResponse.REFUSED;
        }
        return SaveResponse.REFUSED;
    }

    private DeleteResponse translateStorageResp(DeleteNoteResponse resp) {
        switch (resp) {
            case OK:
                return DeleteResponse.OK;
            case REFUSED:
                return DeleteResponse.REFUSED;
        }
        return DeleteResponse.REFUSED;
    }

}
