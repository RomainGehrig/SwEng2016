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
    private String mTitle = new String();
    private String mContent = new String();
    
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
        this.mTitle = title;
    }

    public void setContent(String content) {
        this.mContent = content;
    }
}
