package icynote.plugins;

import android.app.Activity;
import android.text.SpannableString;

import icynote.note.Note;
import icynote.ui.contracts.PluginPresenter;

public class PluginData {
    private Activity activity;
    private PluginPresenter.Contract contractor;

    private Note<SpannableString> lastOpenedNote = null;
    private int selectionStart = -1;
    private int selectionEnd = -1;

    public PluginData(Activity a) {
        activity = a;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Note<SpannableString> getLastOpenedNote() {
        return lastOpenedNote;
    }

    public void setLastOpenedNote(Note<SpannableString> lastOpenedNote) {
        this.lastOpenedNote = lastOpenedNote;
    }

    public int getSelectionStart() {
        return selectionStart;
    }

    public void setSelectionStart(int selectionStart) {
        this.selectionStart = selectionStart;
    }

    public int getSelectionEnd() {
        return selectionEnd;
    }

    public void setSelectionEnd(int selectionEnd) {
        this.selectionEnd = selectionEnd;
    }

    public PluginPresenter.Contract getContractor() {
        return contractor;
    }

    public void setContractor(PluginPresenter.Contract contractor) {
        this.contractor = contractor;
    }
}
