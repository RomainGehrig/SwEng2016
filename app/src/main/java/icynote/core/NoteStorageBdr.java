package icynote.core;

import java.util.List;

/**
 * Boundary
 * used by the application to obtain and store data
 */
public interface NoteStorageBdr {
    List<NotePreviewData> previewAllNotes();
    Optional<NoteData> getNote(int id);

    enum SaveNoteResponse {OK, REFUSED}
    SaveNoteResponse saveNote(NoteData toSave);

    enum UpdateTitleResponse {OK, REFUSED}
    UpdateTitleResponse updateTitle(int id, String title);

    enum DeleteNoteResponse {OK, REFUSED}
    DeleteNoteResponse deleteNote(int id);
}
