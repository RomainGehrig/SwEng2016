package icynote.ui;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import icynote.ui.loginactivities.LoginMenu;

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
        onView(withId(R.id.local_sign_in_button)).perform(click());

        // bad input first
        badMailTest();
        badPassWordTest();

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


    public void badMailTest() throws InterruptedException {
        onView(withId(R.id.field_email)).perform(replaceText("somemail"));
        onView(withId(R.id.field_password)).perform(replaceText("password")).perform(closeSoftKeyboard());
        Thread.sleep(100);
        onView(withId(R.id.email_sign_in_button)).perform(click());

        // check a view in local log in is shown
        onView(withId(R.id.field_email)).check(matches(isDisplayed()));
    }


    public void badPassWordTest() throws InterruptedException {
        onView(withId(R.id.field_email)).perform(replaceText("test@icynote.ch"));
        onView(withId(R.id.field_password)).perform(replaceText("password")).perform(closeSoftKeyboard());
        Thread.sleep(100);
        onView(withId(R.id.email_sign_in_button)).perform(click());

        // check a view in local log in is shown
        onView(withId(R.id.field_email)).check(matches(isDisplayed()));
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

}