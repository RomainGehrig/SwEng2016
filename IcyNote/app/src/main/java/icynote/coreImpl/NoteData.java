package icynote.coreImpl;

import java.util.GregorianCalendar;

import icynote.core.Note;
import icynote.core.Response;


/**
 * Simple data-structure with no validation logic
 * that stores a note's fields. This class does not check
 * whether the fields are set to {@code null} or not.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class NoteData implements Note {

    private int id = 0;
    private String title = "";
    private String content = "";
    private GregorianCalendar creation = new GregorianCalendar();
    private GregorianCalendar lastUpdate = new GregorianCalendar();

    public NoteData() {

    }

    public NoteData(Note toCopy) {
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
    public Response setId(int id) {
        this.id = id;
        return ResponseFactory.positiveResponse();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Response setTitle(String title) {
        this.title = title;
        return ResponseFactory.positiveResponse();
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public Response setContent(String content) {
        this.content = content;
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
    public Response setCreation(GregorianCalendar creation) {
        this.creation = creation;
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
    public Response setLastUpdate(GregorianCalendar lastUpdate) {
        this.lastUpdate = lastUpdate;
        return ResponseFactory.positiveResponse();
    }
}
