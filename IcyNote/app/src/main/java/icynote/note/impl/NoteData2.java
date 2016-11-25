package icynote.note.impl;

import android.text.SpannableString;

import java.util.GregorianCalendar;

import icynote.note.Note;
import icynote.note.Response;
import icynote.note.common.ResponseFactory;


/**
 * Simple data-structure with no validation logic
 * that stores a note's fields. This class does not check
 * whether the fields are set to {@code null} or not.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
@SuppressWarnings("UseOfClone")
public class NoteData2 implements Note<SpannableString> {
    private int id = 0;
    private SpannableString title = new SpannableString("");
    private SpannableString content = new SpannableString("");
    private GregorianCalendar creation = new GregorianCalendar();
    private GregorianCalendar lastUpdate = new GregorianCalendar();

    public NoteData2() {

    }

    public NoteData2(Note<SpannableString> toCopy) {
        id = toCopy.getId();
        title = toCopy.getTitle();
        content = toCopy.getContent();
        creation = toCopy.getCreation();
        lastUpdate = toCopy.getLastUpdate();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Response setId(int newId) {
        id = newId;
        return ResponseFactory.positiveResponse();
    }

    @Override
    public SpannableString getTitle() {
        return title;
    }

    @Override
    public Response setTitle(SpannableString newTitle) {
        title = newTitle;
        return ResponseFactory.positiveResponse();
    }

    @Override
    public SpannableString getContent() {
        return content;
    }

    @Override
    public Response setContent(SpannableString newContent) {
        content = newContent;
        return ResponseFactory.positiveResponse();
    }

    @Override
    public GregorianCalendar getCreation() {
        if (creation == null) {
            return null;
        }
        return (GregorianCalendar) creation.clone();
    }

    @Override
    public Response setCreation(GregorianCalendar creationDate) {
        creation = creationDate;
        return ResponseFactory.positiveResponse();
    }

    @Override
    public GregorianCalendar getLastUpdate() {
        if (lastUpdate == null) {
            return null;
        }
        return (GregorianCalendar) lastUpdate.clone();
    }

    @Override
    public Response setLastUpdate(GregorianCalendar lastUpdateDate) {
        lastUpdate = lastUpdateDate;
        return ResponseFactory.positiveResponse();
    }
}