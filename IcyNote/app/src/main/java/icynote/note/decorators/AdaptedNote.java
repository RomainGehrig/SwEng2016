package icynote.note.decorators;

import java.util.GregorianCalendar;

import icynote.note.Note;
import icynote.note.Response;
import icynote.note.impl.NoteData;
import util.Adapter;

public class AdaptedNote<S, T> implements Note<S> {
    private final Note<T> delegate;
    private final Adapter<S, T> adapter;

    public AdaptedNote(Note<T> toAdapt, Adapter<S, T> usingAdapter) {
        delegate = toAdapt;
        adapter = usingAdapter;
    }

    //-------------------------------------

    @Override
    public NoteData<S> getRaw() {
        NoteData<T> raw = delegate.getRaw();
        NoteData<S> adapted = new NoteData<>(null, null);
        adapted.setId(raw.getId());
        adapted.setTitle(adapter.to(raw.getTitle()));
        adapted.setContent(adapter.to(raw.getContent()));
        adapted.setCreation(raw.getCreation());
        adapted.setLastUpdate(raw.getLastUpdate());
        return adapted;
    }

    @Override
    public int getId() {
        return delegate.getId();
    }

    @Override
    public S getTitle() {
        return adapter.to(delegate.getTitle());
    }

    @Override
    public S getContent() {
        return adapter.to(delegate.getContent());
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
    public Response setTitle(S newTitle) {
        return delegate.setTitle(adapter.from(newTitle));
    }

    @Override
    public Response setContent(S newContent) {
        return delegate.setContent(adapter.from(newContent));
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
