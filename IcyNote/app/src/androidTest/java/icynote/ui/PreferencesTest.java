
package icynote.ui;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import icynote.login.LoginManager;
import icynote.login.LoginManagerFactory;
import icynote.ui.login_activities.EmailPasswordLogin;
import icynote.ui.login_activities.GoogleSignIn;
import icynote.ui.login_activities.LoginMenu;
import util.Callback2;


import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.PositionAssertions.isAbove;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.anything;

public class PreferencesTest {

    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws InterruptedException {
        MainActivity mActivity = main.getActivity();
        LoginManager loginManager = LoginManagerFactory.getInstance();
        loginManager.login("test@icynote.ch", "icynote", null);
    }

    @Test
    public void beginWith3Notes() throws Exception {
        setUpForSorting();
        assertEquals(3, getNotesCount());
    }

    @Test
    public void sortingByTitleAscendingTest () throws Exception {
        setUpForSorting();
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.settings)).perform(click());
        onView(withText("Sort by")).perform(click());
        onView(withText("Title")).perform(click());
        onView(withText("Sort order")).perform(click());
        onView(withText("Ascending")).perform(click());

        Espresso.pressBack();
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());

        //assertEquals(3, getNotesCount()); TODO decomment when bug fixed
        onView(withText("note1")).check(isAbove(withText("note2")));
        onView(withText("note2")).check(isAbove(withText("note3")));
    }

    @Test
    public void sortingByCreatedAscendingTest () throws Exception {
        setUpForSorting();
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.settings)).perform(click());
        onView(withText("Sort by")).perform(click());
        onView(withText("Created")).perform(click());
        onView(withText("Sort order")).perform(click());
        onView(withText("Ascending")).perform(click());

        Espresso.pressBack();
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());

        //assertEquals(3, getNotesCount()); TODO decomment when bug fixed
        onView(withText("note1")).check(isAbove(withText("note3")));
        onView(withText("note3")).check(isAbove(withText("note2")));
    }

    @Test
    public void sortingByLastModifiedAscendingTest () throws Exception {
        setUpForSorting();
        onView(withText("note3")).perform(click());
        onView(withId(R.id.noteDisplayBodyText)).perform(replaceText("new body"));

        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.settings)).perform(click());
        onView(withText("Sort by")).perform(click());
        onView(withText("Last modified")).perform(click());
        onView(withText("Sort order")).perform(click());
        onView(withText("Ascending")).perform(click());

        Espresso.pressBack();
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());

        //assertEquals(3, getNotesCount()); TODO decomment when bug fixed
        onView(withText("note1")).check(isAbove(withText("note3")));
        onView(withText("note3")).check(isAbove(withText("note2")));
    }

    @Test
    public void sortingByTitleDescendingTest () throws Exception {
        setUpForSorting();
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.settings)).perform(click());
        onView(withText("Sort by")).perform(click());
        onView(withText("Title")).perform(click());
        onView(withText("Sort order")).perform(click());
        onView(withText("Descending")).perform(click());

        Espresso.pressBack();
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());

        //assertEquals(3, getNotesCount()); TODO decomment when bug fixed
        onView(withText("note3")).check(isAbove(withText("note2")));
        onView(withText("note2")).check(isAbove(withText("note1")));
    }

    @Test
    public void sortingByCreatedDescendingTest () throws Exception {
        setUpForSorting();
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.settings)).perform(click());
        onView(withText("Sort by")).perform(click());
        onView(withText("Created")).perform(click());
        onView(withText("Sort order")).perform(click());
        onView(withText("Descending")).perform(click());

        Espresso.pressBack();
        onView(withId(R.id.menuButtonImage)).perform(click());
        Thread.sleep(500);
        onView(withText(R.string.listAllNotes)).perform(click());

        //assertEquals(3, getNotesCount()); TODO decomment when bug fixed
        onView(withText("note2")).check(isAbove(withText("note3")));
        onView(withText("note3")).check(isAbove(withText("note1")));
    }

    @Test
    public void sortingByLastModifiedDescendingTest () throws Exception {
        setUpForSorting();
        onView(withText("note3")).perform(click());
        onView(withId(R.id.noteDisplayBodyText)).perform(replaceText("new body"));

        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.settings)).perform(click());
        onView(withText("Sort by")).perform(click());
        onView(withText("Last modified")).perform(click());
        onView(withText("Sort order")).perform(click());
        onView(withText("Descending")).perform(click());

        Espresso.pressBack();
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());

        //assertEquals(3, getNotesCount()); TODO decomment when bug fixed
        onView(withText("note2")).check(isAbove(withText("note3")));
        onView(withText("note3")).check(isAbove(withText("note1")));
    }


    //---- Helper methods -----------


    public void setUpForSorting() throws Exception {
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());
        deleteAllNotes();
        addNote("note1", "body1");
        addNote("note3", "body3");
        addNote("note2", "body2");
    }

    private void addNote(String title, String body) {
        onView(withId(R.id.btAdd)).perform(click());
        onView(withId(R.id.noteDisplayTitleText)).perform(replaceText(title));
        onView(withId(R.id.noteDisplayBodyText)).perform(replaceText(body));
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());
    }

    private void deleteNote(int indexNote) {
        onData(anything()).inAdapterView(withId(R.id.lvNotes))
                .atPosition(indexNote)
                .onChildView(withId(R.id.checkBox))
                .perform(click());
        onView(withId(R.id.btDelete)).perform(click());
    }

    private void restoreNote(int indexNote) {
        onData(anything()).inAdapterView(withId(R.id.lvNotes))
                .atPosition(indexNote)
                .onChildView(withId(R.id.checkBox))
                .perform(click());
        onView(withId(R.id.btRestore)).perform(click());
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

    private int getNotesCount() {
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
