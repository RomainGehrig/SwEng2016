package icynote.exporters;

import android.content.Context;
import android.text.SpannableString;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import icynote.note.Note;
import icynote.plugins.PluginData;

public interface NoteExporter<T extends NoteExporter.ExportedNote> {
    T export(Note<SpannableString> note, Context context);

    interface ExportedNote extends Serializable {
        byte[] getBytes();
    }
}
