package icynote.core.impl;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import icynote.core.IcyNoteCore;
import icynote.core.Note;
import icynote.storage.ListStorage;
import icynote.storage.SQLiteStorage;
import icynote.ui.BuildConfig;

/**
 * A Singleton Factory around IcyNoteCore that handles users login and logout.
 */
@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
public final class CoreSingleton {
    private static IcyNoteCore instance = null;

    public static class NotLoggedInException extends RuntimeException {
        public NotLoggedInException() {
            super("a user must be logged in before accessing the core");
        }
    }

    /**
     * Reload the core with the data of the user corresponding to the provided UUID.
     * @param userUID the current user's unique ID.
     */
    public static void login(Context context, String userUID) {
        log("opening core instance for user " + userUID);
        instance = CoreFactory.core(new SQLiteStorage(context, userUID));
    }

    /**
     * Indicates whether a user is already logged in.
     * @return true when a user is logged in, false otherwise.
     */
    public static boolean isLoggedIn() {
        return instance != null;
    }

    /**
     * Close user account and unload its data from the core.
     */
    public static void logout() {
        instance = null;
        log("closing current core instance");
    }

    /**
     * Returns the current core instance.
     * Pre condition: a user has been logged in with {@code login(String userUID)}. <br>
     * @return the current core instance containing the notes of the current user.
     */
    public static IcyNoteCore getCore() {
        if (instance == null) {
            throw new NotLoggedInException();
        }
        return instance;
    }

    private static void log(String message) {
        if (BuildConfig.DEBUG) {
           Log.d("icynote.CoreSingleton", message);
        }
    }

    /**
     * Always throws an {@code AssertionError}.
     * It exists for coverage purposes only.
     * @throws AssertionError always
     */
    @VisibleForTesting
    CoreSingleton() {
        throw new AssertionError("This is an utility class");
    }
}
