package icynote.login;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;


@RunWith(AndroidJUnit4.class)
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