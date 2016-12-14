package icynote.note.impl;

import android.text.SpannableString;


/**
 * Simple data-structure with no validation logic
 * that stores a note's fields. This class does not check
 * whether the fields are set to {@code null} or not.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
@SuppressWarnings("UseOfClone")
public class NoteData2 extends NoteData<SpannableString> {
    /**
     * Instantiates a new Note data 2.
     */
    public NoteData2() {
        super(new SpannableString(""), new SpannableString(""));
    }
}