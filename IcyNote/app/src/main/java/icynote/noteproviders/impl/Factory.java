package icynote.noteproviders.impl;


import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.text.SpannableString;

import java.util.Map;
import java.util.TreeMap;

import icynote.note.Note;
import icynote.note.decorators.AdaptedNote;
import icynote.note.decorators.DateDecorator;
import icynote.note.decorators.NoteDecoratorFactory;
import icynote.note.decorators.NullInput;
import icynote.note.decorators.NullOutput;
import icynote.noteproviders.NoteProvider;
import icynote.noteproviders.decorators.AdaptedProvider;
import icynote.noteproviders.decorators.CheckedNoteProvider;
import icynote.noteproviders.decorators.NoteWithInteractorsProvider;
import icynote.noteproviders.persistent.SQLiteNoteProvider;
import util.Adapter;

/**
 * Simple factory class with default configuration for a NoteProvider with decorators.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
public final class Factory {

    public static <S> CheckedNoteProvider<Note<S>> addNullChecks(NoteProvider<Note<S>> persistent) {
        return new CheckedNoteProvider<>(persistent);
    }

    public static <S> NoteWithInteractorsProvider<S>
    addNoteDecorators(NoteProvider<Note<S>> persistent) {
        NoteWithInteractorsProvider<S> core = new NoteWithInteractorsProvider<S>(persistent);
        core.stack(new NoteDecoratorFactory<S>() {
            @Override
            public Note<S> make(Note<S> delegate) {
                Note<S> tmp = delegate;
                tmp = new NullOutput<>(tmp);
                tmp = new DateDecorator<>(tmp);
                tmp = new NullInput<>(tmp);
                return tmp;
            }
        });
        return core;
    }

    public static NoteProvider<Note<SpannableString>> make(
            Context c,
            String userUID,
            NoteDecoratorFactory<SpannableString> formatters)
    {
        SQLiteNoteProvider sqlite = new SQLiteNoteProvider(c, userUID);

        NoteProvider<Note<SpannableString>> adapted =
                new AdaptedProvider<>(sqlite, new NoteAdapter());

        NoteWithInteractorsProvider<SpannableString> noteChecked = addNoteDecorators(adapted);
        noteChecked.stack(formatters);

        return addNullChecks(noteChecked);
    }

    /**
     * Always throws an {@code AssertionError}.
     * It exists for coverage purposes only.
     * @throws AssertionError always
     */
    @VisibleForTesting
    public Factory() {
        throw new AssertionError("This is an utility class");
    }

    //------------------------------------------------------------------------------------------

    private final static Adapter<SpannableString, String> strAdapter =
            new Adapter<SpannableString, String>() {
        @Override
        public SpannableString to(String b) {
            return new SpannableString(b);
        }
        @Override
        public String from(SpannableString a) {
            return a.toString();
        }
    };

    private static class NoteAdapter implements Adapter<Note<SpannableString>, Note<String>> {
        private Map<Integer, AdaptedNote<SpannableString, String>> cache = new TreeMap<>();

        @Override
        public Note<SpannableString> to(Note<String> b) {
            if (!cache.containsKey(b.getId())) {
                cache.put(b.getId(), new AdaptedNote<>(b,  strAdapter));
            }
            return cache.get(b.getId());
        }

        @Override
        public Note<String> from(Note<SpannableString> a) {
            return cache.get(a.getId()).getAdaptedNote();
        }
    }
}