package icynote.plugins;

import android.app.Activity;
import android.text.SpannableString;

import icynote.note.Note;
import icynote.ui.contracts.PluginPresenter;

/**
 * Plugin data
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class PluginData {
    private Activity activity;
    private PluginPresenter.Contract contractor;

    private Note<SpannableString> lastOpenedNote = null;
    private int selectionStart = -1;
    private int selectionEnd = -1;

    /**
     * Instantiates a new Plugin data.
     *
     * @param a the activity
     */
    public PluginData(Activity a) {
        activity = a;
    }

    /**
     * Gets activity.
     *
     * @return the activity
     */
    public Activity getActivity() {
        return activity;
    }

    /**
     * Sets activity.
     *
     * @param activity the activity
     */
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    /**
     * Gets the last opened note.
     *
     * @return the last opened note
     */
    public Note<SpannableString> getLastOpenedNote() {
        return lastOpenedNote;
    }

    /**
     * Sets last opened note.
     *
     * @param lastOpenedNote the last opened note
     */
    public void setLastOpenedNote(Note<SpannableString> lastOpenedNote) {
        this.lastOpenedNote = lastOpenedNote;
    }

    /**
     * Gets selection start.
     *
     * @return the selection start
     */
    public int getSelectionStart() {
        return selectionStart;
    }

    /**
     * Sets selection start.
     *
     * @param selectionStart the selection start
     */
    public void setSelectionStart(int selectionStart) {
        this.selectionStart = selectionStart;
    }

    /**
     * Gets selection end.
     *
     * @return the selection end
     */
    public int getSelectionEnd() {
        return selectionEnd;
    }

    /**
     * Sets selection end.
     *
     * @param selectionEnd the selection end
     */
    public void setSelectionEnd(int selectionEnd) {
        this.selectionEnd = selectionEnd;
    }

    /**
     * Gets contractor.
     *
     * @return the contractor
     */
    public PluginPresenter.Contract getContractor() {
        return contractor;
    }

    /**
     * Sets contractor.
     *
     * @param contractor the contractor
     */
    public void setContractor(PluginPresenter.Contract contractor) {
        this.contractor = contractor;
    }
}
