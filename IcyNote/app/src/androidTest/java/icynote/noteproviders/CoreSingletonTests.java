package icynote.noteproviders;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import icynote.noteproviders.impl.Singleton;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CoreSingletonTests {
    private Context context = InstrumentationRegistry.getTargetContext();

    @Before
    public void setUp() throws Exception {
        Singleton.logout();
    }

    private void login(String userUID) {
        Singleton.login(context, userUID);
    }
    
    @Test(expected=AssertionError.class)
    public void createFactory(){
        Singleton c = new Singleton();
        assertNull(c);
    }

    @Test
    public void login() {
        login("a");
    }

    @Test
    public void isLoggedInWhenYes() {
        login("a");
        assertTrue(Singleton.isLoggedIn());
    }

    @Test
    public void isLoggedInWhenNo() {
        Singleton.logout();
        assertFalse(Singleton.isLoggedIn());
    }

    @Test
    public void logout() {
        login("a");
        assertTrue(Singleton.isLoggedIn());
        Singleton.logout();
        assertFalse(Singleton.isLoggedIn());
    }

    @Test
    public void getCore() {
        login("a");
        assertNotNull(Singleton.getCore());
    }

    @Test(expected = Singleton.NotLoggedInException.class)
    public void getCoreWhenLoggedOut() {
        Singleton.logout();
        Singleton.getCore();
    }

}