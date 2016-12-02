package icynote.ui.loginactivities;

import android.os.IBinder;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.Root;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.WindowManager;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import icynote.ui.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class LocalLogInTest {

    private LoginMenu mActivity;

    @Rule
    public ActivityTestRule<LoginMenu> main = new ActivityTestRule<>(LoginMenu.class);

    @Before
    public void setUp() throws Exception {
        mActivity = main.getActivity();
    }


    @Test
    public void localLogInSuccessTest() throws InterruptedException {
        logOutIfAlreadyLogInTest();
        onView(ViewMatchers.withId(R.id.local_sign_in_button)).perform(click());

        // bad input first
        badMailTest();
        badPassWordTest();
        mailRequiredTest();
        passwordRequiredTest();

        // then success login
        successfulLogIn();

        // logout to be able to continue login test
        logOutIfAlreadyLogInTest();
    }

    private void successfulLogIn() throws InterruptedException {
        onView(withId(R.id.field_email)).perform(replaceText("test@icynote.ch"));
        onView(withId(R.id.field_password)).perform(replaceText("icynote")).perform(closeSoftKeyboard());
        Thread.sleep(100);
        onView(withId(R.id.email_sign_in_button)).perform(click());
        Thread.sleep(2000);

        // check a view in mainactivity is shown
        onView(withId(R.id.searchBar)).check(matches(isDisplayed()));
    }


    public void mailRequiredTest() throws InterruptedException {
        onView(withId(R.id.field_email)).perform(replaceText(""));
        onView(withId(R.id.field_password)).perform(replaceText("password")).perform(closeSoftKeyboard());
        Thread.sleep(100);
        onView(withId(R.id.email_sign_in_button)).perform(click());

        // check a view in local log in is shown
        onView(withId(R.id.field_email)).check(matches(isDisplayed()));
    }


    public void passwordRequiredTest() throws InterruptedException {
        onView(withId(R.id.field_email)).perform(replaceText("test@icynote.ch"));
        onView(withId(R.id.field_password)).perform(replaceText("")).perform(closeSoftKeyboard());
        Thread.sleep(100);
        onView(withId(R.id.email_sign_in_button)).perform(click());

        // check a view in local log in is shown
        onView(withId(R.id.field_email)).check(matches(isDisplayed()));
    }


    public void badMailTest() throws InterruptedException {
        onView(withId(R.id.field_email)).perform(replaceText("somemail"));
        onView(withId(R.id.field_password)).perform(replaceText("password")).perform(closeSoftKeyboard());
        Thread.sleep(100);
        onView(withId(R.id.email_sign_in_button)).perform(click());
        Thread.sleep(1000);
        // check a view in local log in is shown
        onView(withId(R.id.field_email)).check(matches(isDisplayed()));
        onView(withText("The email address is badly formatted.")).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }


    public void badPassWordTest() throws InterruptedException {
        onView(withId(R.id.field_email)).perform(replaceText("test@icynote.ch"));
        onView(withId(R.id.field_password)).perform(replaceText("password")).perform(closeSoftKeyboard());
        Thread.sleep(100);
        onView(withId(R.id.email_sign_in_button)).perform(click());
        Thread.sleep(1000);
        // check a view in local log in is shown
        onView(withId(R.id.field_email)).check(matches(isDisplayed()));
        onView(withText("The password is invalid or the user does not have a password.")).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }


    private void logOutIfAlreadyLogInTest() { // test log out too
        try {
            onView(withId(R.id.menuButtonImage)).perform(click());
            onView(withText(R.string.logout)).perform(click());
            onView(withId(R.id.local_sign_in_button)).check(matches(isDisplayed()));
        }
        catch (NoMatchingViewException e) {
            // was already logged out
        }
    }

    private class ToastMatcher extends TypeSafeMatcher<Root> {
        @Override public void describeTo(Description description) {
            description.appendText("is toast");
        }

        @Override public boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                if (windowToken == appToken) {
                    //means this window isn't contained by any other windows.
                    return true;
                }
            }
            return false;
        }
    }

}