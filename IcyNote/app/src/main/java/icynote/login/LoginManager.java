package icynote.login;

import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import icynote.core.impl.CoreSingleton;
import util.Optional;

/**
 * This class wraps the Firebase Auth. library in order to completely isolate the rest of the application from it.
 * This allows for testing the UI without directly interacting with the firebase server.
 * Furthermore, if we want to change the library, this is the ONLY class to update.
 *
 * @author Julien Harbulot
 * @version 1.0
 *
 */
public class LoginManager {

    public static class Callback {
        public void execute() {}
    }

    public static class Callback2<A,B> {
        public void execute(A a, B b) {}
    }

    public static class NotLoggedInException extends RuntimeException {
        public NotLoggedInException() {
            super("a user must be logged in before accessing the core");
        }
    }

    /** Executed when the user signs in. */
    private Callback onLoginCallback;

    /** Executed when the user signs out. */
    private Callback onLogoutCallback;

    /** The underlying library we use to manager user's accounts. */
    private FirebaseAuth mAuth;

    /** Package protected method: use a factory to create a LoginManager. */
    LoginManager() {
        onLoginCallback = new Callback();
        onLogoutCallback = new Callback();
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new AuthStateListener());
    }

    /**
     * Indicates whether there is a logged in user.
     */
    public boolean userIsLoggedIn() {
        return (mAuth.getCurrentUser() != null);
    }
    /**
     * Returns the current user Unique ID (UID) or throws a {@code NotLoggedInException} if no user is logged in.
     * Pre condition: a user has been logged in.
     * @throws CoreSingleton.NotLoggedInException if no user is logged in.
     * @return the current user's Unique ID (UID).
     */
    public String getCurrentUserUID() {
        if (mAuth.getCurrentUser() == null) {
            throw new LoginManager.NotLoggedInException();
        }
        return mAuth.getCurrentUser().getUid();
    }

    /**
     * Returns the current user email or throws a {@code NotLoggedInException} if no user is logged in.
     * Pre condition: a user has been logged in.
     * @throws CoreSingleton.NotLoggedInException if no user is logged in.
     * @return the current user's email.
     */
    public String getCurrentUserEmail() {
        if (mAuth.getCurrentUser() == null) {
            throw new LoginManager.NotLoggedInException();
        }
        return mAuth.getCurrentUser().getEmail();
    }
    /**
     * Creates a new user account.
     * @param email email of the user (should not be null)
     * @param password password of the user (should not be null)
     * @param onCompleteCallback callback to execute when the creation finished;
     *                           the first Boolean argument indicates whether the creation was successful;
     *                           the second String argument contains an error message if unsuccessful.
     *                           If the callback is `null` then nothing will be executed on completion.
     */
    public void createAccount(String email, String password,
                              Callback2<Boolean, String> onCompleteCallback)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListenerWithCallback(onCompleteCallback));
    }

    /**
     * Deletes the current account; cannot be undone.
     * @param onCompleteCallback callback to execute when the creation finished;
     *                           the first Boolean argument indicates whether the creation was successful;
     *                           the second String argument contains an error message if unsuccessful.
     *                           If the callback is `null` then nothing will be executed on completion.
     */
    public void deleteAccount(final Callback onCompleteCallback) {
        if (mAuth.getCurrentUser() == null) {
            throw new LoginManager.NotLoggedInException();
        }
        mAuth.getCurrentUser().delete()
                .addOnCompleteListener(new  OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (onCompleteCallback != null) {
                            onCompleteCallback.execute();
                        }
                    }
                });
    }
    /**
     * Signs the user in given an existing email/password combination.
     * @param email email of the user (should not be null)
     * @param password password of the user (should not be null)
     * @param onCompleteCallback callback to execute when the creation finished;
     *                           the first Boolean argument indicates whether the login was successful;
     *                           the second String argument contains an error message if unsuccessful.
     *                           If the callback is `null` then nothing will be executed on completion.
     */
    public void login(String email, String password,
                              Callback2<Boolean, String> onCompleteCallback)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListenerWithCallback(onCompleteCallback));
    }

    /**
     * Signs the user in given a valid Google Sign In account.
     * @param account a valid Google Sign In account, obtained via google API.
     * @param onCompleteCallback callback to execute when the creation finished;
     *                           the first Boolean argument indicates whether the login was successful;
     *                           the second String argument contains an error message if unsuccessful.
     *                           If the callback is `null` then nothing will be executed on completion.
     */
    public void login(GoogleSignInAccount account, Callback2<Boolean, String> onCompleteCallback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListenerWithCallback(onCompleteCallback));
    }
    /** Signs the current user out. */
    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    /**
     * Sets the callback to be executed when a user signs in.
     * If `callback` is `null`, then nothing will be executed.
     */
    public void onLogin(Callback callback){
        if (callback == null) {
            callback = new Callback(); //a null-object that does nothing
        }
        onLoginCallback = callback;
    }

    /**
     * Sets the callback to be executed when a user signs in.
     * If `callback` is `null`, then nothing will be executed.
     */
    public void onLogout(Callback callback){
        if (callback == null) {
            callback = new Callback(); //a null-object that does nothing
        }
        onLogoutCallback = callback;
    }


    /**
     * Adapter design pattern: calls `onLoginCallback` when user logs in, and `onLogoutCallback` when user
     * logs out.
     */
    private class AuthStateListener implements FirebaseAuth.AuthStateListener {
        @Override
        public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                onLoginCallback.execute();
            } else {
                onLogoutCallback.execute();
            }
        }
    }

    /**
     * Adapter design pattern: translate our Callback2 to OnCompleteListener.
     */
    private static class OnCompleteListenerWithCallback implements OnCompleteListener<AuthResult> {
        private Callback2<Boolean, String> callback;

        public OnCompleteListenerWithCallback(Callback2<Boolean, String> c){
            if (c == null) {
                c = new Callback2<>(); //null-object that does nothing
            }
            callback = c;
        }

        @Override
        public void onComplete(Task<AuthResult> task) {
            if (task.isSuccessful()) {
                callback.execute(true, "");
            } else {
                callback.execute(false, task.getException().getMessage());
            }
        }
    }
}
