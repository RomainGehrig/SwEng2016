package icynote.login;

import android.support.annotation.VisibleForTesting;

/**
 * Implements a pattern singleton around LoginManager.
 */
@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
public final class LoginManagerFactory {
    private static  LoginManager instance = new LoginManager();

    /**
     * Returns the current instance of the LoginManager.
     */
    public static LoginManager getInstance() {
        return instance;
    }

    /**
     * Should be used to mock the LoginManager when testing the UI.
     * @throws IllegalArgumentException if {@code otherInstance} is {@code null}.
     */
    public static void setInstance(LoginManager otherInstance) {
        if (otherInstance == null) {
            throw new IllegalArgumentException("LoginManager can't be null");
        }
        instance = otherInstance;
    }

    /**
     * Always throws an {@code AssertionError}.
     * It exists for coverage purposes only.
     * @throws AssertionError always
     */
    @VisibleForTesting
    LoginManagerFactory() {
        throw new AssertionError("LoginManagerFactory is an utility class");
    }
}
