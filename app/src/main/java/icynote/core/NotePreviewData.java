package icynote.core;

import java.util.Date;

/**
 * light-weight parts of a note useful for preview
 */
public class NotePreviewData {
    private final int m_id;
    private final String m_title;
    private final Date m_creation;
    private final Date m_lastModification;
    
    public NotePreviewData(int id, 
            String title, 
            Date creation, 
            Date last) {
        m_id = id;
        m_title = title;
        m_creation = creation;
        m_lastModification = last;
    }

    public int getId() {
        return m_id;
    }

    public String getTitle() {
        return m_title;
    }

    public Date getCreation() {
        return (Date)m_creation.clone();
    }

    public Date getLastModification() {
        return (Date)m_lastModification.clone();
    }
}
