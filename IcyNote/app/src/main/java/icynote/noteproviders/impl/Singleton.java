package icynote.noteproviders.impl;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.text.SpannableString;
import android.util.Log;

import icynote.note.Note;
import icynote.note.decorators.NoteDecoratorFactory;
import icynote.noteproviders.NoteProvider;
import icynote.plugins.FormatterPlugin;
import icynote.plugins.PluginsProvider;
import icynote.ui.BuildConfig;

/**
 * A Singleton Factory around NoteProvider that handles users login and logout.
 */
@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
public final class Singleton {
    private static NoteProvider<Note<SpannableString>> instance = null;

    public static class NotLoggedInException extends RuntimeException {
        public NotLoggedInException() {
            super("a user must be logged in before accessing the addNoteDecorators");
        }
    }

    /**
     * Reload the addNoteDecorators with the data of the user corresponding to the provided UUID.
     * @param userUID the current user's unique ID.
     */
    public static void login(Context context, String userUID) {
        log("opening addNoteDecorators instance for user " + userUID);

        NoteDecoratorFactory<SpannableString> temp = new NoteDecoratorFactory<>();
        

        instance = Factory.make(context, userUID, temp);
    }

    /**
     * Indicates whether a user is already logged in.
     * @return true when a user is logged in, false otherwise.
     */
    public static boolean isLoggedIn() {
        return instance != null;
    }

    /**
     * Close user account and unload its data from the addNoteDecorators.
     */
    public static void logout() {
        instance = null;
        log("closing current addNoteDecorators instance");
    }

    /**
     * Returns the current addNoteDecorators instance.
     * Pre condition: a user has been logged in with {@code login(String userUID)}. <br>
     * @return the current addNoteDecorators instance containing the notes of the current user.
     */
    public static NoteProvider<Note<SpannableString>> getCore() {
        if (instance == null) {
            throw new NotLoggedInException();
        }
        return instance;
    }

    private static void log(String message) {
        if (BuildConfig.DEBUG) {
           Log.d("icynote.Singleton", message);
        }
    }

    /**
     * Always throws an {@code AssertionError}.
     * It exists for coverage purposes only.
     * @throws AssertionError always
     */
    @VisibleForTesting
    public Singleton() {
        throw new AssertionError("This is an utility class");
    }
}
