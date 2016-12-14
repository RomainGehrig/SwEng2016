package icynote.ui.utils;

import icynote.note.Response;

/**
 * The interface Can delete note.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public interface CanDeleteNote {
    /**
     * Delete note response.
     *
     * @param noteId the note id to be deleted
     * @return the response message
     */
    Response deleteNote(int noteId);
}
