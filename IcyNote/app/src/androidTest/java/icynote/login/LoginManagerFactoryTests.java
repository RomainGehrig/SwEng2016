package icynote.login;

import org.junit.Test;

import static org.junit.Assert.*;

public class LoginManagerFactoryTests {

    @Test(expected=AssertionError.class)
    public void createAFactory() {
        LoginManagerFactory f = new LoginManagerFactory();
        assertNull(f);
    }
    @Test
    public void getInstance() throws Exception {
        LoginManager m = LoginManagerFactory.getInstance();
        assertNotNull(m);

        LoginManager m2 = LoginManagerFactory.getInstance();
        assertEquals(m, m2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void changeInstanceWhenNull() throws Exception {
        LoginManagerFactory.setInstance(null);
    }

    @Test
    public void changeInstanceWhenNotNull() throws Exception {
        LoginManager newInstance = new LoginManager();
        assertNotSame("before set", newInstance, LoginManagerFactory.getInstance());

        LoginManagerFactory.setInstance(newInstance);
        assertSame("after set", newInstance, LoginManagerFactory.getInstance());
    }

}