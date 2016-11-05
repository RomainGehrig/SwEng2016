package icynote.core.impl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreSingletonTests {
    @Before
    public void setUp() {
        CoreSingleton.logout();
    }

    @Test(expected=AssertionError.class)
    public void createFactory(){
        CoreSingleton c = new CoreSingleton();
        assertNull(c);
    }

    @Test
    public void login() {
        CoreSingleton.login("a");
    }

    @Test
    public void isLoggedInWhenYes() {
        CoreSingleton.login("a");
        assertTrue(CoreSingleton.isLoggedIn());
    }

    @Test
    public void isLoggedInWhenNo() {
        CoreSingleton.logout();
        assertFalse(CoreSingleton.isLoggedIn());
    }

    @Test
    public void logout() {
        CoreSingleton.login("a");
        assertTrue(CoreSingleton.isLoggedIn());
        CoreSingleton.logout();
        assertFalse(CoreSingleton.isLoggedIn());
    }

    @Test
    public void getCore() {
        CoreSingleton.login("a");
        assertNotNull(CoreSingleton.getCore());
    }

    @Test(expected = CoreSingleton.NotLoggedInException.class)
    public void getCoreWhenLoggedOut() {
        CoreSingleton.logout();
        CoreSingleton.getCore();
    }

}