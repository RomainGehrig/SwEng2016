package icynote.core;

import java.util.Date;

/**
 * data and meta-data of a full note
 * provided by the storage
 */
public class NoteData {
    private final int m_id;
    private final Date m_creation;
    private final Date m_lastModification;
    private String m_title = new String();
    private String m_content = new String();
    
    public NoteData(int id, 
            Date creation, 
            Date last) {
        m_id = id;
        m_creation = creation;
        m_lastModification = last;

    }

    public int getId() {
        return m_id;
    }

    public Date getCreation() {
        return (Date) m_creation.clone();
    }

    public Date getLastModification() {
        return (Date) m_lastModification.clone();
    }

    public String getTitle() {
        return m_title;
    }

    public String getContent() {
        return m_content;
    }

    public void setTitle(String title) {
        this.m_title = title;
    }

    public void setContent(String content) {
        this.m_content = content;
    }
}
