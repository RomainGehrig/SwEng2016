package icynote.note.decorators;

import android.text.SpannableString;

import java.util.GregorianCalendar;

import icynote.note.Note;
import icynote.note.Response;


public class SpannableAdapter implements Note<SpannableString> {
    private Note<String> delegate;

    public SpannableAdapter(Note<String> delegateNote) {
        delegate = delegateNote;
    }

    @Override
    public int getId() {
        return delegate.getId();
    }

    @Override
    public SpannableString getTitle() {
        return new SpannableString(delegate.getTitle());
    }

    @Override
    public SpannableString getContent() {
        return new SpannableString(delegate.getContent());
    }

    @Override
    public GregorianCalendar getCreation() {
        return delegate.getCreation();
    }

    @Override
    public GregorianCalendar getLastUpdate() {
        return delegate.getLastUpdate();
    }

    @Override
    public Response setId(int newId) {
        return delegate.setId(newId);
    }

    @Override
    public Response setTitle(SpannableString newTitle) {
        return delegate.setTitle("" + newTitle);
    }

    @Override
    public Response setContent(SpannableString newContent) {
        return delegate.setContent("" + newContent);
    }

    @Override
    public Response setCreation(GregorianCalendar creationDate) {
        return delegate.setCreation(creationDate);
    }

    @Override
    public Response setLastUpdate(GregorianCalendar lastUpdateDate) {
        return delegate.setLastUpdate(lastUpdateDate);
    }
}
