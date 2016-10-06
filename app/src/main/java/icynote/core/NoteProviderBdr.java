package icynote.core;

import java.util.List;

/**
 * Boundary
 * used by the UI to obtain note preview or editable notes
 */
public interface NoteProviderBdr {
    List<NotePreviewData> previewAllNotes();
    icynote.core.Optional<EditableNoteBdr> getEditableNote(int id);
}
