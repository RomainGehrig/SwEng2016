package icynote.core.impl;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CoreSingletonTests {
    private Context context = InstrumentationRegistry.getTargetContext();

    @Before
    public void setUp() throws Exception {
        CoreSingleton.logout();
    }

    private void login(String userUID) {
        CoreSingleton.login(context, userUID);
    }
    
    @Test(expected=AssertionError.class)
    public void createFactory(){
        CoreSingleton c = new CoreSingleton();
        assertNull(c);
    }

    @Test
    public void login() {
        login("a");
    }

    @Test
    public void isLoggedInWhenYes() {
        login("a");
        assertTrue(CoreSingleton.isLoggedIn());
    }

    @Test
    public void isLoggedInWhenNo() {
        CoreSingleton.logout();
        assertFalse(CoreSingleton.isLoggedIn());
    }

    @Test
    public void logout() {
        login("a");
        assertTrue(CoreSingleton.isLoggedIn());
        CoreSingleton.logout();
        assertFalse(CoreSingleton.isLoggedIn());
    }

    @Test
    public void getCore() {
        login("a");
        assertNotNull(CoreSingleton.getCore());
    }

    @Test(expected = CoreSingleton.NotLoggedInException.class)
    public void getCoreWhenLoggedOut() {
        CoreSingleton.logout();
        CoreSingleton.getCore();
    }

}