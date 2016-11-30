package icynote.ui.loginactivities;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import icynote.ui.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class GoogleSignInTest {

    @Rule
    public IntentsTestRule<LoginMenu> main = new IntentsTestRule<>(LoginMenu.class);


    @Before
    public void stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal()))
                .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }


    @Test
    public void googleSignInTest() throws InterruptedException {

        logOutIfAlreadyLogInTest();
        onView(withId(R.id.googe_sign_in_button)).perform(click());
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(GoogleSignIn.RC_GOOGLE_SIGN_IN, null);

        //intended(toPackage("com.google.android.gms"));
        intending(toPackage("com.google.android.gms")).respondWith(result);
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