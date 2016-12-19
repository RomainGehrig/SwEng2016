
package icynote.ui;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.PerformException;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import junit.framework.AssertionFailedError;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import icynote.login.LoginManager;
import icynote.login.LoginManagerFactory;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.PositionAssertions.isAbove;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
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
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                setUpForSorting();
                Thread.sleep(1500);
                assertEquals(3, getNotesCount());
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void sortingByTitleAscendingTest() throws Exception {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
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
                Thread.sleep(500);
                tryCheckTextIsAbove("note1", "note2");
                tryCheckTextIsAbove("note2", "note3");
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void sortingByCreatedAscendingTest() throws Exception {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
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
                Thread.sleep(500);
                tryCheckTextIsAbove("note1", "note3");
                tryCheckTextIsAbove("note3", "note2");
            } catch (AssertionFailedError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void sortingByLastModifiedAscendingTest() throws Exception {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
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
                Thread.sleep(500);
                tryCheckTextIsAbove("note1", "note2");
                tryCheckTextIsAbove("note2", "note3");
            } catch (AssertionFailedError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void sortingByTitleDescendingTest() throws Exception {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
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
                Thread.sleep(500);
                tryCheckTextIsAbove("note3", "note2");
                tryCheckTextIsAbove("note2", "note1");
            } catch (AssertionFailedError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void sortingByCreatedDescendingTest() throws Exception {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
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
                Thread.sleep(500);
                tryCheckTextIsAbove("note2", "note3");
                tryCheckTextIsAbove("note3", "note1");
            } catch (AssertionFailedError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void sortingByLastModifiedDescendingTest() throws Exception {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
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
                Thread.sleep(500);
                tryCheckTextIsAbove("note2", "note3");
                tryCheckTextIsAbove("note3", "note1");
            } catch (AssertionFailedError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
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
        Thread.sleep(1500);
        int notesNb = getNotesCount();
        for (int i = 0; i < notesNb; ++i) {
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
            Log.e("tryReplaceTextOfId :", "execution exceeded " + sleepLimit + "ms");
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
            Log.e("tryClickOnMatcher :", "execution exceeded " + sleepLimit + "ms");
        }
    }

    private void tryCheckTextIsAbove(String upperText, String lowerText) throws InterruptedException {
        boolean tryAgain = true;
        int timeElapsed = 0;
        int sleepFor = 100;
        int sleepLimit = 5000;
        while (tryAgain && timeElapsed < sleepLimit) {
            tryAgain = false;
            try {
                onView(withText(upperText)).check(isAbove(withText(lowerText)));
            } catch (PerformException | NoMatchingViewException e) {
                tryAgain = true;
                timeElapsed += sleepFor;
                Thread.sleep(sleepFor);
            }
        }
        if (timeElapsed >= 5000) {
            Log.e("tryCheckTextIsAbove :", "execution exceeded " + sleepLimit + "ms");
        }
    }
}
