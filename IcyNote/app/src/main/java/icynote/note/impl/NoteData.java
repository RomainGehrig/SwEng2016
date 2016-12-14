package icynote.note.impl;

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
public class NoteData<S> implements Note<S> {
    private int id = 0;
    private S title;
    private S content;
    private GregorianCalendar creation = new GregorianCalendar();
    private GregorianCalendar lastUpdate = new GregorianCalendar();

    /**
     * Instantiates a new Note data with default title and content.
     *
     * @param defaultTitle
     * @param defaultContent
     */
    public NoteData(S defaultTitle, S defaultContent) {
        title = defaultTitle;
        content = defaultContent;
    }

    /**
     * Instantiates a new Note data from a note to be copied.
     *
     * @param toCopy the note to be copied
     */
    public NoteData(Note<S> toCopy) {
        id = toCopy.getId();
        title = toCopy.getTitle();
        content = toCopy.getContent();
        creation = toCopy.getCreation();
        lastUpdate = toCopy.getLastUpdate();
    }

    @Override
    public NoteData<S> getRaw() {
        return this;
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
    public S getTitle() {
        return title;
    }

    @Override
    public Response setTitle(S newTitle) {
        title = newTitle;
        return ResponseFactory.positiveResponse();
    }

    @Override
    public S getContent() {
        return content;
    }

    @Override
    public Response setContent(S newContent) {
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