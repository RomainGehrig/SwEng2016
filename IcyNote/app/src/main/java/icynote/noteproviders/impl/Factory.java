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
import icynote.note.decorators.NullStringCorrector;
import icynote.note.impl.NoteData;
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
                tmp = new NullOutput<>(tmp, "site: persistent delegate output");
                tmp = new DateDecorator<>(tmp);
                tmp = new NullInput<>(tmp, "site: date decorator input");
                return tmp;
            }
        });
        return core;
    }

    public static NoteProvider<Note<SpannableString>> make(
            Context c,
            String userUID,
            final NoteDecoratorFactory<SpannableString> formatters)
    {
        SQLiteNoteProvider sqlite = new SQLiteNoteProvider(c, userUID);

        NoteWithInteractorsProvider<String> sqliteChecked =
                new NoteWithInteractorsProvider<>(sqlite);
        sqliteChecked.stack(new NoteDecoratorFactory<String>() {
            @Override
            public Note<String> make(Note<String> delegate) {
                //delegate = new NullOutput<>(delegate, "site: sqlite output");
                //delegate = new NullInput<>(delegate, "site: sqlite input");
                delegate = new NullStringCorrector<String>(delegate, "sqlite",
                        new NullStringCorrector.Corrector<String>() {
                    @Override
                    public String makeCorrection() {
                        return "{null}";
                    }
                });
                return delegate;

            }
        });

        NoteProvider<Note<SpannableString>> adapted =
                new AdaptedProvider<>(sqliteChecked, new NoteAdapter());
        //new SpannableAdapterProvider(sqliteChecked);

        NoteWithInteractorsProvider<SpannableString> noteChecked = addNoteDecorators(adapted);
        noteChecked.stack(new NoteDecoratorFactory<SpannableString>(){
            @Override
            public Note<SpannableString> make(Note<SpannableString> delegate) {
                Note<SpannableString> tmp = delegate;
                tmp = new NullOutput<>(tmp, "site: interactors delegate output");
                tmp = new NullInput<>(tmp, "site: interactors delegate input");
                tmp = formatters.make(tmp);
                tmp = new NullOutput<>(tmp, "site: formatters output");
                tmp = new NullInput<>(tmp, "site: formatters input");
                return tmp;
            }
        });

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
            return "" + a;
        }
    };

    private static class NoteAdapter implements Adapter<Note<SpannableString>, Note<String>> {
        private Map<Integer, AdaptedNote<SpannableString, String>> cache = new TreeMap<>();

        @Override
        public Note<SpannableString> to(Note<String> b) {
            return new AdaptedNote<>(b, strAdapter);
        }

        @Override
        public Note<String> from(Note<SpannableString> a) {
            return Factory.trim(a, strAdapter);
        }
    }

    public static <S> NoteData trim(Note<S> note, Adapter<S, String> strAdapter) {
        NoteData trimmed = new NoteData();
        trimmed.setId(note.getId());
        trimmed.setTitle(strAdapter.from(note.getTitle()));
        trimmed.setContent(strAdapter.from(note.getContent()));
        trimmed.setLastUpdate(note.getLastUpdate());
        trimmed.setCreation(note.getCreation());
        return trimmed;
    }
}