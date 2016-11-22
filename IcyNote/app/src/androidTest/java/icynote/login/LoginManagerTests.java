package icynote.login;

import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Timestamp;
import java.util.concurrent.CountDownLatch;

import util.Callback;
import util.Callback2;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;


@SuppressWarnings("ClassWithTooManyMethods")
@RunWith(AndroidJUnit4.class)
public class LoginManagerTests {
    private LoginManager manager = null;
    private String email = null;
    private String password = null;
    private String knownEmail = "test@icynote.ch";
    private String knownPassword = "icynote";

    @Rule
    public UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

    @Before
    public void setUp() throws Throwable {
        email = createEmail();
        password = "pass.word0123";
        manager = new LoginManager();
        logout();
    }

    @Test
    public void loginTest() throws Throwable {
        email = knownEmail;
        password = knownPassword;
        login(true);
        assertTrue("user is logged in", manager.userIsLoggedIn());

        logout();
        assertFalse("user is logged out", manager.userIsLoggedIn());
    }

    @Test
    public void onLoginTest() throws Throwable {
        email = knownEmail;
        password = knownPassword;
        onLogin();
        assertTrue("user is logged in", manager.userIsLoggedIn());

        logout();
        assertFalse("user is logged out", manager.userIsLoggedIn());
    }

    @Test
    public void loginWithInvalidPassword() throws Throwable {
        email = knownEmail;
        password = knownPassword + "unexpectedSuffix";
        login(false);
    }

    @Test
    public void loginWithUnknownAccount() throws Throwable {
        login(false);
        assertFalse("user is logged in", manager.userIsLoggedIn());
    }

    @Test
    public void createAlreadyExistingAccount() throws Throwable {
        email = knownEmail;
        password = knownPassword;
        create(false);
        assertFalse("user is logged in", manager.userIsLoggedIn());
    }

    @Test
    public void createAccountWithIllFormedEmail() throws Throwable {
        email = "no.arobase.ch";
        create(false);
        assertFalse("user is logged in", manager.userIsLoggedIn());
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void createAccountWithEmptyEmail() throws Throwable {
        email = "";
        create(false);
        assertFalse("user is logged in", manager.userIsLoggedIn());
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void createAccountWithEmptyPassword() throws Throwable {
        password = "";
        create(false);
    }

    @Test
    public void createThenDeleteAccount() throws Throwable {
        create(true);
        assertTrue("creation => login", manager.userIsLoggedIn());

        delete();
        assertFalse("deletion => logout", manager.userIsLoggedIn());
    }

    @Test
    public void getAccountInfo() throws Throwable {
        email = knownEmail;
        password = knownPassword;
        login(true);
        assertTrue("logged in", manager.userIsLoggedIn());
        assertNotNull("uuid", manager.getCurrentUserUID());
        assertEquals("email", email, manager.getCurrentUserEmail());
    }

    @Test(expected = LoginManager.NotLoggedInException.class)
    public void getUUIDWhenLoggedOut() throws Throwable {
        manager.getCurrentUserUID();
    }

    @Test(expected = LoginManager.NotLoggedInException.class)
    public void getEmailWhenLoggedOut() throws Throwable {
        manager.getCurrentUserEmail();
    }

    @Test(expected = LoginManager.NotLoggedInException.class)
    public void deleteWhenLoggedOut() throws Throwable {
        manager.deleteAccount(null);
    }

    @Test
    public void onLogoutNullArgument() throws Throwable {
        manager.onLogout(null); //should not throw any exception
    }

    @Test
    public void userCanLoginWithTest() throws Throwable {
        create(true);
        assertTrue("creation => login", manager.userIsLoggedIn());

        assertTrue("can login with email", manager.userCanLoginWithEmail());
        assertFalse("can login with google", manager.userCanLoginWithGoogle());

        delete();
        assertFalse("deletion => logout", manager.userIsLoggedIn());
    }


    @Test
    public void unlinkGoogleWhenNotLinkedTest() throws Throwable {
        create(true);
        assertTrue("creation => login", manager.userIsLoggedIn());

        assertTrue(manager.unlinkGoogleAccount());

        delete();
        assertFalse("deletion => logout", manager.userIsLoggedIn());
    }


    //-------------------------------------------------------------------------------


    private String createEmail() {
        return "test."
                + timestamp()
                .replaceAll("[^\\d.]", "")
                + "@icynote.ch";
    }
    private String timestamp() {
        java.util.Date date = new java.util.Date();
        return (new Timestamp(date.getTime()))
                .toString();
    }

    //-------------------------------------------------------------------------------

    private void create(final Boolean expected) throws Throwable {
        final CountDownLatch latch = new CountDownLatch(1);
        uiThreadTestRule.runOnUiThread(new Runnable() {
            public void run() {
                manager.createAccount(email, password,
                        new Callback2<Boolean, String>() {
                            @Override
                            public void execute(Boolean success, String message) {
                                assertEquals("creation [" + message + "]", expected, success);
                                latch.countDown();
                            }
                        });
            }
        });
        latch.await();
    }

    private void login(final GoogleSignInAccount a, final Boolean expected) throws Throwable {
        final CountDownLatch latch = new CountDownLatch(1);
        uiThreadTestRule.runOnUiThread(new Runnable() {
            public void run() {
                manager.onLogin(null);
                manager.login(a, new Callback2<Boolean, String>(){
                    @Override
                    public void execute(Boolean success, String error) {
                        assertEquals("login [" + error + "]", expected, success);
                        latch.countDown();
                    }
                });
            }
        });
        latch.await();
    }

    private void login(final Boolean expected) throws Throwable {
        final CountDownLatch latch = new CountDownLatch(1);
        uiThreadTestRule.runOnUiThread(new Runnable() {
            public void run() {
                manager.onLogin(null);
                manager.login(email, password, new Callback2<Boolean, String>(){
                    @Override
                    public void execute(Boolean success, String error) {
                        assertEquals("login [" + error + "]", expected, success);
                        latch.countDown();
                    }
                });
            }
        });
        latch.await();
    }

    private void logout() throws Throwable {
        final CountDownLatch latch = new CountDownLatch(1);
        uiThreadTestRule.runOnUiThread(new Runnable() {
            public void run() {
                manager.onLogout(new Callback(){
                    @Override
                    public void execute() {
                        latch.countDown();
                    }
                });
                manager.logout();
            }
        });
        latch.await();
    }

    private void onLogin() throws Throwable {
        final CountDownLatch latch = new CountDownLatch(1);
        uiThreadTestRule.runOnUiThread(new Runnable() {
            public void run() {
                manager.onLogin(new Callback(){
                    @Override
                    public void execute() {
                        latch.countDown();
                    }
                });
                manager.login(email, password, null);
            }
        });
        latch.await();
    }

    private void delete() throws Throwable {
        final CountDownLatch latch = new CountDownLatch(1);
        uiThreadTestRule.runOnUiThread(new Runnable() {
            public void run() {
                manager.deleteAccount(new Callback() {
                    @Override
                    public void execute() {
                        latch.countDown();
                    }
                });
            }
        });
        latch.await();
    }
}