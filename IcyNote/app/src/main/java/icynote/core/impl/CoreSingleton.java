package icynote.core.impl;

import android.support.annotation.VisibleForTesting;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import icynote.core.IcyNoteCore;
import icynote.core.Note;
import icynote.storage.ListStorage;
import icynote.ui.BuildConfig;

/**
 * A Singleton Factory around IcyNoteCore that handles users login and logout.
 */
@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
public final class CoreSingleton {
    public static class NotLoggedInException extends RuntimeException {
        public NotLoggedInException() {
            super("a user must be logged in before accessing the core");
        }
    }

    /**
     * Reload the core with the data of the user corresponding to the provided UUID.
     * @param userUID the current user's unique ID.
     */
    public static void login(String userUID) {
        initializeDefaultAccount();

        if (!cores.containsKey(userUID)) {
            cores.put(userUID, CoreFactory.core(new ListStorage()));
            log("creating core for " + userUID);
        }
        instance = cores.get(userUID);
        log("getting core for " + userUID);
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
    private static IcyNoteCore instance = null;
    private static Map<String, IcyNoteCore> cores = new HashMap<>();

    /**
     * Debug method: creates a few notes for the user `test@icynote.ch`.
     */
    private static void initializeDefaultAccount() {
        final String defaultUUID = "zb3VspTPCmNshPDH9zb8iE7mxxP2";
        if (!cores.containsKey(defaultUUID)) {
            cores.put(defaultUUID, CoreFactory.core(new ListStorage()));
            IcyNoteCore c = cores.get(defaultUUID);

            String[] titles = new String[]{
                    "monday morning", "todo", "ideas"
            };
            String[] contents = new String[]{
                    "I went to the market!", "SwEng Quizz", "Ad impossibilia nemo tenetur"
            };

            for (int i = 0; i < titles.length; ++i) {
                Note n = c.createNote().get();
                n.setTitle(titles[i]);
                n.setContent(contents[i]);
                c.persist(n);
            }
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
