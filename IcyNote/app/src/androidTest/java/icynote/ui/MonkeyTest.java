package icynote.ui;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import icynote.ui.R;
import icynote.ui.loginactivities.LoginMenu;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;

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

        deleteAllNotes();
        addNote("note1", "body1");
        addNote("note2", "body2");
        addNote("note3", "body3");

        assertEquals(3, getNotesCount());

        deleteNote(1);

        assertEquals(2, getNotesCount());
        onView(withText("note2")).check(doesNotExist());
        onView(withText("note1")).check(matches(isDisplayed()));
        onView(withText("note3")).check(matches(isDisplayed()));



/*        onView(withId(R.id.btDelete)).check(matches(isDisplayed()));
        onView(withId(R.id.btDelete)).perform(click());*/

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

    private void deleteNote(int indexNote) {
        onData(anything()).inAdapterView(withId(R.id.lvNotes))
                .atPosition(1)
                .onChildView(withId(R.id.checkBox))
                .perform(click());
        onView(withId(R.id.btDelete)).perform(click());
    }

    private void addNote(String title, String body) {
        onView(withId(R.id.btAdd)).perform(click());
        onView(withId(R.id.noteDisplayTitleText)).perform(replaceText(title));
        onView(withId(R.id.noteDisplayBodyText)).perform(replaceText(body));
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());
    }

    private void deleteAllNotes() {
        for (int i = 0 ; i < getNotesCount() ; ++i) {
            onData(anything()).inAdapterView(withId(R.id.lvNotes))
                    .atPosition(i)
                    .onChildView(withId(R.id.checkBox))
                    .perform(click());
        }
        onView(withId(R.id.btDelete)).perform(click());
    }

    public int getNotesCount()
    {
        final int[] counts = new int[1];
        onView(withId(R.id.lvNotes)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                ListView listView = (ListView) view;

                counts[0] = listView.getCount();

                return true;
            }

            @Override
            public void describeTo(Description description) {

            }
        }));

        return counts[0];
    }
}

