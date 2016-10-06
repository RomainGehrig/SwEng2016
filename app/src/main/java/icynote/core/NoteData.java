package icynote.core;

import java.util.Date;

/**
 * data and meta-data of a full note
 * provided by the storage
 */
public class NoteData {
    private final int mId;
    private final Date mCreation;
    private final Date mLastModification;
    private String mTitle = "";
    private String mContent = "";
    
    public NoteData(int id, 
            Date creation, 
            Date last) {
        mId = id;
        mCreation = creation;
        mLastModification = last;

    }

    public int getId() {
        return mId;
    }

    public Date getCreation() {
        return (Date) mCreation.clone();
    }

    public Date getLastModification() {
        return (Date) mLastModification.clone();
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setContent(String content) {
        mContent = content;
    }
}
