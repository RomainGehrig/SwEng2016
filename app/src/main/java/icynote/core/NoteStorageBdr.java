package icynote.core;

import java.util.List;

/**
 * Boundary
 * used by the application to obtain and store data
 */
public interface NoteStorageBdr {
    List<NotePreviewData> previewAllNotes();
    Optional<NoteData> getNote(int id);
    boolean saveNote(NoteData toSave);
    boolean updateTitle(int id, String title);
    boolean deleteNote(int id);
}
