package icynote.core.impl;

import android.os.Parcel;
import android.os.Parcelable;

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
@SuppressWarnings("UseOfClone")
public class NoteData implements Note, Parcelable {

    public static final Parcelable.Creator<NoteData> CREATOR = new Parcelable.Creator<NoteData>() {
        @Override
        public NoteData createFromParcel(Parcel source) {
            return new NoteData(source);
        }

        @Override
        public NoteData[] newArray(int size) {
            return new NoteData[size];
        }
    };

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


    public NoteData(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
        creation = (GregorianCalendar)in.readSerializable();
        lastUpdate = (GregorianCalendar)in.readSerializable();
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
    public String getTitle() {
        return title;
    }

    @Override
    public Response setTitle(String newTitle) {
        title = newTitle;
        return ResponseFactory.positiveResponse();
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public Response setContent(String newContent) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeSerializable(creation);
        dest.writeSerializable(lastUpdate);
    }
}