package icynote.ui;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import icynote.ui.R;
import icynote.ui.loginactivities.LoginMenu;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

public class MonkeyTest {

    private LoginMenu mActivity;

    @Rule
    public ActivityTestRule<LoginMenu> main = new ActivityTestRule<>(LoginMenu.class);

    @Before
    public void setUp() throws Exception {
        mActivity = main.getActivity();
    }

    @Test
    public void monkeyTest() throws InterruptedException {
        if (mActivity.findViewById(R.id.field_email) != null) {
            onView(withId(R.id.field_email)).perform(replaceText("test@icynote.ch"));
            onView(withId(R.id.field_password))
                    .perform(replaceText("icynote")).perform(closeSoftKeyboard());
        }

        onView(withId(R.id.menuButtonImage)).check(matches(isDisplayed()));
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());

        onData(anything()).inAdapterView(withId(R.id.lvNotes))
                          .atPosition(0)
                          .onChildView(withId(R.id.checkBox))
                          .perform(click());

        onView(withId(R.id.btDelete)).check(matches(isDisplayed()));
        onView(withId(R.id.btDelete)).perform(click());

/*        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());*/

        /*
        onView(withId(R.id.btAdd)).perform(click());
        onView(withId(R.id.noteDisplayTitleText)).check(matches(isDisplayed()));
        onView(withId(R.id.noteDisplayTitleText)).perform(replaceText("note1"));
        onView(withId(R.id.noteDisplayBodyText)).perform(replaceText("body1"));

        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.trash)).perform(click());

        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.openLastNote)).perform(click());

        onView(withId(R.id.noteDisplayTitleText)).check(matches(withText("note1")));

        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());

        onView(withText("note1")).perform(click());
        onView(withId(R.id.noteDisplayBodyText)).check(matches(withText("body1")));
*/
    }

}
