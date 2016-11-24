package icynote.noteproviders.decorators;


import android.text.SpannableString;

import java.util.Iterator;

import icynote.note.Note;
import icynote.note.Response;
import icynote.note.decorators.SpannableAdapter;
import icynote.note.impl.NoteData;
import icynote.noteproviders.NoteProvider;
import icynote.noteproviders.OrderBy;
import icynote.noteproviders.OrderType;
import util.Optional;

public class SpannableAdapterProvider implements NoteProvider<Note<SpannableString>> {
    private NoteProvider<Note<String>> delegate;

    public SpannableAdapterProvider(NoteProvider<Note<String>> delegateProvider) {
        delegate = delegateProvider;
    }

    @Override
    public Optional<Note<SpannableString>> createNote() {
        Optional<Note<String>> created = delegate.createNote();
        if (created.isPresent()) {
            Note<SpannableString> adapted = new SpannableAdapter(created.get());
            return Optional.of(adapted);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Note<SpannableString>> getNote(int id) {
        Optional<Note<String>> obtained = delegate.getNote(id);
        if (obtained.isPresent()) {
            Note<SpannableString> adapted = new SpannableAdapter(obtained.get());
            return Optional.of(adapted);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Note<SpannableString>> getNotes(OrderBy index, OrderType order) {
        final Iterable<Note<String>> origin = delegate.getNotes(index, order);
        final Iterator<Note<String>> it = origin.iterator();
        return new Iterable<Note<SpannableString>>() {
            @Override
            public Iterator<Note<SpannableString>> iterator() {
                return new Iterator<Note<SpannableString>>() {
                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public Note<SpannableString> next() {
                        return new SpannableAdapter(it.next());
                    }
                };
            }
        };
    }

    @Override
    public Response persist(Note<SpannableString> spannableStringNote) {
        NoteData trimmed = new NoteData();
        trimmed.setId(spannableStringNote.getId());
        trimmed.setTitle("" + spannableStringNote.getTitle());
        trimmed.setContent("" + spannableStringNote.getContent());
        trimmed.setLastUpdate(spannableStringNote.getLastUpdate());
        trimmed.setCreation(spannableStringNote.getCreation());
        return delegate.persist(trimmed);
    }

    @Override
    public Response delete(int id) {
        return delegate.delete(id);
    }
}
