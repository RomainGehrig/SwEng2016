package icynote.core;

import java.util.Date;

/**
 * light-weight parts of a note useful for preview
 */
public class NotePreviewData {
    private final int mId;
    private final String mTitle;
    private final Date mCreation;
    private final Date mLastModification;
    
    public NotePreviewData(int id, 
            String title, 
            Date creation, 
            Date last) {
        mId = id;
        mTitle = title;
        mCreation = creation;
        mLastModification = last;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public Date getCreation() {
        return (Date)mCreation.clone();
    }

    public Date getLastModification() {
        return (Date)mLastModification.clone();
    }
}
