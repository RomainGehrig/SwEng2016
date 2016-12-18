
package icynote.ui;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
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

    private MainActivity mActivity;

    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws InterruptedException {
        mActivity = main.getActivity();
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
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.settings);
        tryClickOnText("Sort by");
        tryClickOnText("Title");
        tryClickOnText("Sort order");
        tryClickOnText("Ascending");

        Espresso.pressBack();
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);

        //assertEquals(3, getNotesCount()); TODO decomment when bug fixed
        Thread.sleep(1000);
        onView(withText("note1")).check(isAbove(withText("note2")));
        onView(withText("note2")).check(isAbove(withText("note3")));
    }

    @Test
    public void sortingByCreatedAscendingTest () throws Exception {
        setUpForSorting();
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.settings);
        tryClickOnText("Sort by");
        tryClickOnText("Created");
        tryClickOnText("Sort order");
        tryClickOnText("Ascending");

        Espresso.pressBack();
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);

        //assertEquals(3, getNotesCount()); TODO decomment when bug fixed
        Thread.sleep(1000);
        onView(withText("note1")).check(isAbove(withText("note3")));
        onView(withText("note3")).check(isAbove(withText("note2")));
    }

    /*@Test
    public void sortingByLastModifiedAscendingTest () throws Exception {
        setUpForSorting();
        tryClickOnText("note3");
        tryClickOnId(R.id.noteDisplayBodyText);
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.settings);
        tryClickOnText("Sort by");
        tryClickOnText("Last modified");
        tryClickOnText("Sort order");
        tryClickOnText("Ascending");

        Espresso.pressBack();
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);

        //assertEquals(3, getNotesCount()); TODO decomment when bug fixed
        Thread.sleep(1000);
        onView(withText("note1")).check(isAbove(withText("note3")));
        onView(withText("note3")).check(isAbove(withText("note2")));
    }*/

    @Test
    public void sortingByTitleDescendingTest () throws Exception {
        setUpForSorting();
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.settings);
        tryClickOnText("Sort by");
        tryClickOnText("Title");
        tryClickOnText("Sort order");
        tryClickOnText("Descending");

        Espresso.pressBack();
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);

        //assertEquals(3, getNotesCount()); TODO decomment when bug fixed
        Thread.sleep(1000);
        onView(withText("note3")).check(isAbove(withText("note2")));
        onView(withText("note2")).check(isAbove(withText("note1")));
    }

    @Test
    public void sortingByCreatedDescendingTest () throws Exception {
        setUpForSorting();
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.settings);
        tryClickOnText("Sort by");
        tryClickOnText("Created");
        tryClickOnText("Sort order");
        tryClickOnText("Descending");

        Espresso.pressBack();
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);

        //assertEquals(3, getNotesCount()); TODO decomment when bug fixed
        Thread.sleep(1000);
        onView(withText("note2")).check(isAbove(withText("note3")));
        onView(withText("note3")).check(isAbove(withText("note1")));
    }

    @Test
    public void sortingByLastModifiedDescendingTest () throws Exception {
        setUpForSorting();
        tryClickOnText("note3");
        tryReplaceTextOfId(R.id.noteDisplayBodyText, "new body");

        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.settings);
        tryClickOnText("Sort by");
        tryClickOnText("Last modified");
        tryClickOnText("Sort order");
        tryClickOnText("Descending");

        Espresso.pressBack();
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);

        //assertEquals(3, getNotesCount()); TODO decomment when bug fixed
        Thread.sleep(1000);
        onView(withText("note2")).check(isAbove(withText("note3")));
        onView(withText("note3")).check(isAbove(withText("note1")));
    }


    //---- Helper methods -----------


    public void setUpForSorting() throws Exception {
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);
        deleteAllNotes();
        addNote("note1", "body1");
        addNote("note3", "body3");
        addNote("note2", "body2");
    }

    private void addNote(String title, String body) throws InterruptedException {
        tryClickOnId(R.id.btAdd);
        tryReplaceTextOfId(R.id.noteDisplayTitleText, title);
        tryReplaceTextOfId(R.id.noteDisplayBodyText, body);
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);
    }

    private void deleteAllNotes() throws InterruptedException {
        for (int i = 0 ; i < getNotesCount() ; ++i) {
            Thread.sleep(500);
            onData(anything()).inAdapterView(withId(R.id.lvNotes))
                    .atPosition(i)
                    .onChildView(withId(R.id.checkBox))
                    .perform(click());
        }
        tryClickOnId(R.id.btDelete);
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

    private void tryClickOnText(final int resourceId) throws InterruptedException {
        tryClickOnMatcher(withText(resourceId));
    }

    private void tryClickOnText(String text) throws InterruptedException {
        tryClickOnMatcher(withText(text));
    }

    private void tryClickOnId(final int resourceId) throws InterruptedException {
        tryClickOnMatcher(withId(resourceId));
    }

    private void tryReplaceTextOfId(final int resourceId, String newText) throws InterruptedException {
        boolean tryAgain = true;
        int timeElapsed = 0;
        int sleepFor = 100;
        int sleepLimit = 5000;
        while (tryAgain && timeElapsed < sleepLimit) {
            tryAgain = false;
            try {
                onView(withId(resourceId)).perform(replaceText(newText));
            } catch (PerformException | NoMatchingViewException e) {
                tryAgain = true;
                timeElapsed += sleepFor;
                Thread.sleep(sleepFor);
            }
        }
        if (timeElapsed >= 5000) {
            Log.e("tryClickOnText :", "execution exceeded " + sleepLimit + "ms");
        }
    }

    private void tryClickOnMatcher(Matcher<View> matcher) throws InterruptedException {
        boolean tryAgain = true;
        int timeElapsed = 0;
        int sleepFor = 100;
        int sleepLimit = 5000;
        while (tryAgain && timeElapsed < sleepLimit) {
            tryAgain = false;
            try {
                onView(matcher).perform(click());
            } catch (PerformException | NoMatchingViewException e) {
                tryAgain = true;
                timeElapsed += sleepFor;
                Thread.sleep(sleepFor);
            }
        }
        if (timeElapsed >= 5000) {
            Log.e("tryClickOnText :", "execution exceeded " + sleepLimit + "ms");
        }
    }
}
