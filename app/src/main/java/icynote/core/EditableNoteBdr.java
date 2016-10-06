package icynote.core;

import java.util.Date;

/**
 * Boundary
 * used by the UI to edit a note
 */
public interface EditableNoteBdr {
    int getId();
    Date getDateOfCreation();
    Date getDateOfLastModification();
    String getTitle();
    String getContent();
    /* ---------------------------------------- */
    boolean setTitle(String newTitle);
    boolean setContent(String newContent);
    /* ---------------------------------------- */
    boolean save();
    boolean delete();
}
