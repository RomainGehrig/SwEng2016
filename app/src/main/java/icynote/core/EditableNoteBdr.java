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

    enum SetTitleResponse {OK, REFUSED}
    SetTitleResponse setTitle(String newTitle);

    enum SetContentResponse {OK, REFUSED}
    SetContentResponse setContent(String newContent);

    /* ---------------------------------------- */

    enum SaveResponse {OK, REFUSED}
    SaveResponse save();

    enum DeleteResponse {OK, REFUSED}
    DeleteResponse delete();
}
