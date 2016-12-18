package icynote.ui;

import android.os.IBinder;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.Root;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.anything;

public class MainActivityTest {

    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        MainActivity mActivity = main.getActivity();
    }

// pipeline 2
    // start app with notes list
    @Test
    public void startingViewTest() throws InterruptedException {
        Thread.sleep(500);
        onView(withId(R.id.menuButtonImage)).check(matches(isDisplayed()));
        onView(withId(R.id.searchBar)).check(matches(isDisplayed()));
        onView(withId(R.id.btAdd)).check(matches(isDisplayed()));
        onView(withId(R.id.btDelete)).check(matches(isDisplayed()));
    }

    @Test
    public void menuContainsAllButtonsTest() throws InterruptedException {
        onView(withId(R.id.menuButtonImage)).perform(click());
        Thread.sleep(500);
        onView(withText(R.string.listAllNotes)).check(matches(isDisplayed()));
        onView(withText(R.string.trash)).check(matches(isDisplayed()));
        onView(withText(R.string.settings)).check(matches(isDisplayed()));
        onView(withText(R.string.logout)).check(matches(isDisplayed()));
    }

    // Open fragment tests
    @Test
    public void openFragmentListNotesTest() throws InterruptedException {
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);
        Thread.sleep(500);
        onView(withId(R.id.menuButtonImage)).check(matches(isDisplayed()));
        onView(withId(R.id.searchBar)).check(matches(isDisplayed()));
        onView(withId(R.id.btAdd)).check(matches(isDisplayed()));
        onView(withId(R.id.btDelete)).check(matches(isDisplayed()));
    }

    @Test
    public void openFragmentTrashTest() throws InterruptedException {
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.trash);
        Thread.sleep(500);
        onView(withId(R.id.menuButtonImage)).check(matches(isDisplayed()));
        onView(withId(R.id.searchBar)).check(matches(isDisplayed()));
        onView(withId(R.id.btRestore)).check(matches(isDisplayed()));
    }

    /*@Test // TODO
    public void openFragmentSettingsTest() {
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.settings)).perform(click());
        assertNotNull(mActivity.findViewById(R.id.menuButtonImage));
    }*/

    /*@Test
    public void openFragmentEditTagsTest() {
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.editTags)).perform(click());
        assertNotNull(mActivity.findViewById(R.id.editTagsView));
        assertNotNull(mActivity.findViewById(R.id.menuButton));
        assertNotNull(mActivity.findViewById(R.id.menuButtonImage));
    }*/


/*
    TODO
    Settings are new in Preferences.java and xml.preferences
    !! Preferences is a PreferenceActivity, not a Fragment
    @Test
    public void openFragmentSettingsTest() {
        assertNull(mActivity.findViewById(R.id.styles_list));
        assertNull(mActivity.findViewById(R.id.styles_title));
        onView(withId(R.id.menuButton)).perform(click());
        onView(withText(R.string.settings)).perform(click());
        assertNotNull(mActivity.findViewById(R.id.styles_list));
        assertNotNull(mActivity.findViewById(R.id.styles_title));
        assertNotNull(mActivity.findViewById(R.id.menuButton));
        assertNotNull(mActivity.findViewById(R.id.menuButtonImage));
    }
*/

    // open metadata in edit note
    @Test
    public void openMetadataTest() throws InterruptedException {
        tryClickOnId(R.id.btAdd);
        tryClickOnId(R.id.note_open_metadata);
        Thread.sleep(500);
        onView(withId(R.id.backButton)).check(matches(isDisplayed()));
        onView(withId(R.id.noteTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.noteCreationDate)).check(matches(isDisplayed()));
        onView(withId(R.id.noteModificationDate)).check(matches(isDisplayed()));
    }

    @Test
    public void findNoteWithOneNoteTest() throws InterruptedException {
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);
        deleteAllNotes();
        addNote("note1", "body1");
        Thread.sleep(500);
        assertEquals(1, getNotesCount());
        tryReplaceTextOfId(R.id.searchBar, "note1-");
        Thread.sleep(500);
        assertEquals(0, getNotesCount());
        tryReplaceTextOfId(R.id.searchBar, "note1");
        Thread.sleep(500);
        assertEquals(1, getNotesCount());
    }

    @Test
    public void findNoteWithDeleteTest() throws InterruptedException {
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);
        deleteAllNotes();
        addNote("note1", "body1");
        addNote("note2", "body2");
        assertEquals(2, getNotesCount());
        tryReplaceTextOfId(R.id.searchBar, "note3");
        Thread.sleep(500);
        assertEquals(0, getNotesCount());
        tryReplaceTextOfId(R.id.searchBar, "note");
        Thread.sleep(500);
        assertEquals(2, getNotesCount());
    }

    @Test
    public void deletionTest() throws InterruptedException {
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);

        deleteAllNotes();
        Thread.sleep(500);
        assertEquals(0, getNotesCount());
        onView(withId(R.id.tvNumNotes)).check(matches(withText("0 notes")));

        addNote("note1", "body1");
        addNote("note2", "body2");
        addNote("note3", "body3");
        Thread.sleep(500);
        assertEquals(3, getNotesCount());
        onView(withId(R.id.tvNumNotes)).check(matches(withText("3 notes")));

        deleteNote(1);
        Thread.sleep(500);
        assertEquals(2, getNotesCount());
        onView(withId(R.id.tvNumNotes)).check(matches(withText("2 notes")));
        onView(withText("note2")).check(doesNotExist());
        onView(withText("note1")).check(matches(isDisplayed()));
        onView(withText("note3")).check(matches(isDisplayed()));
    }

    @Test
    public void reopenLastNoteWhenThereIsNoneTest() throws InterruptedException {
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);
        deleteAllNotes();
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.openLastNote);
        Thread.sleep(500);
        onView(withText("There is no last opened note.")).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void reopenLastNoteDoNotOpenDeletedNoteTest() throws InterruptedException {
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);
        deleteAllNotes();
        addNote("note1", "body1");
        deleteAllNotes();
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.openLastNote);
        Thread.sleep(500);
        onView(withText("There is no last opened note.")).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void reopenLastNoteOpenCorrectNoteTest() throws InterruptedException {
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);
        deleteAllNotes();
        addNote("note1", "body1");
        addNote("note2", "body2");
        addNote("note3", "body3");
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.openLastNote);

        Thread.sleep(500);
        onView(withId(R.id.noteDisplayTitleText)).check(matches(withText("note3")));
        onView(withId(R.id.noteDisplayBodyText)).check(matches(withText("body3")));
    }

    @Test
    public void titleModifiedInMetadataAppearsInListTest() throws InterruptedException {
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);
        deleteAllNotes();
        tryClickOnId(R.id.btAdd);
        tryClickOnId(R.id.note_open_metadata);
        tryReplaceTextOfId(R.id.noteTitle, "note1");
        tryClickOnId(R.id.backButton);
        Thread.sleep(500);
        onView(withId(R.id.noteDisplayTitleText)).check(matches(withText("note1")));
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);
        Thread.sleep(500);
        assertEquals(1, getNotesCount());
    }

    @Test
    public void titleModifiedInEditNoteAppearsInMetadata() throws InterruptedException {
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);
        deleteAllNotes();
        tryClickOnId(R.id.btAdd);
        tryReplaceTextOfId(R.id.noteDisplayTitleText, "note1");

        tryClickOnId(R.id.note_open_metadata);
        Thread.sleep(500);
        onView(withId(R.id.noteTitle)).check(matches(withText("note1")));
        tryClickOnId(R.id.backButton);
        Thread.sleep(500);
        onView(withId(R.id.noteDisplayTitleText)).check(matches(withText("note1")));
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);
        Thread.sleep(500);
        assertEquals(1, getNotesCount());
    }

    //---- Tests for the trash ------

    /*@Test // TODO : dont work anymore, why ??
    public void findNoteWithOneNoteTrashTest() {
        deleteAllNotes();
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());
        addNote("note1", "body1");
        deleteNote(0);

        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.trash)).perform(click());
        assertEquals(1, getNotesCount());
        onView(withId(R.id.searchBar)).perform(replaceText("note1-"));
        assertEquals(0, getNotesCount());
        onView(withId(R.id.searchBar)).perform(replaceText("note1"));
        assertEquals(1, getNotesCount());
    }*/

    @Test
    public void findNoteWithDeleteTrashTest() throws InterruptedException {
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);
        addNote("note1", "body1");
        addNote("note2", "body2");
        deleteNote(0);
        deleteNote(0);

        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.trash);
        Thread.sleep(500);
        int nbNotes = getNotesCount();
        tryReplaceTextOfId(R.id.searchBar, "note3");
        Thread.sleep(500);
        assertTrue(getNotesCount() <= nbNotes - 2);
        tryReplaceTextOfId(R.id.searchBar, "");
        Thread.sleep(500);
        assertEquals(nbNotes, getNotesCount());
    }

    @Test
    public void deletingANoteAddItToTheTrashTest() throws InterruptedException {
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.trash);
        Thread.sleep(500);
        assertEquals(0, getNotesCount());

        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);
        addNote("note1", "body1");
        deleteNote(0);

        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.trash);
        Thread.sleep(500);
        assertEquals(1, getNotesCount());
    }

    @Test
    public void deletingThreeNotesAddThemToTheTrashTest() throws InterruptedException {
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.trash);
        Thread.sleep(500);
        assertEquals(0, getNotesCount());

        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);
        addNote("note1", "body1");
        addNote("note2", "body2");
        addNote("note3", "body3");
        deleteNote(0);
        deleteNote(0);
        deleteNote(0);

        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.trash);
        Thread.sleep(500);
        assertEquals(3, getNotesCount());
    }

    @Test
    public void restoringANoteTest() throws InterruptedException {
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);
        Thread.sleep(500);
        int numberOfNotes = getNotesCount();
        addNote("note1", "body1");
        Thread.sleep(500);
        assertEquals(1 + numberOfNotes, getNotesCount());
        deleteNote(0);
        Thread.sleep(500);
        assertEquals(numberOfNotes, getNotesCount());

        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.trash);
        Thread.sleep(500);
        assertEquals(1, getNotesCount());
        restoreNote(0);
        Thread.sleep(500);
        assertEquals(0, getNotesCount());

        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);
        Thread.sleep(500);
        assertEquals(1 + numberOfNotes, getNotesCount());
    }

    @Test
    public void restoringThreeNotesTest() throws InterruptedException {
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);
        Thread.sleep(500);
        int numberOfNotes = getNotesCount();
        addNote("note1", "body1");
        addNote("note2", "body2");
        addNote("note3", "body3");
        Thread.sleep(500);
        assertEquals(3+numberOfNotes, getNotesCount());
        deleteNote(0);
        deleteNote(0);
        deleteNote(0);
        Thread.sleep(500);
        assertEquals(numberOfNotes, getNotesCount());

        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.trash);
        Thread.sleep(500);
        assertEquals(3, getNotesCount());
        Thread.sleep(500);
        onData(anything()).inAdapterView(withId(R.id.lvNotes))
                .atPosition(0)
                .onChildView(withId(R.id.checkBox))
                .perform(click());
        Thread.sleep(500);
        onData(anything()).inAdapterView(withId(R.id.lvNotes))
                .atPosition(1)
                .onChildView(withId(R.id.checkBox))
                .perform(click());
        Thread.sleep(500);
        onData(anything()).inAdapterView(withId(R.id.lvNotes))
                .atPosition(2)
                .onChildView(withId(R.id.checkBox))
                .perform(click());
        tryClickOnId(R.id.btRestore);
        Thread.sleep(500);
        assertEquals(0, getNotesCount());

        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);
        Thread.sleep(500);
        assertEquals(3+numberOfNotes, getNotesCount());
    }


    //---- Helper methods -----------

    private void addNote(String title, String body) throws InterruptedException {
        tryClickOnId(R.id.btAdd);
        tryReplaceTextOfId(R.id.noteDisplayTitleText, title);
        Thread.sleep(500);
        onView(withId(R.id.noteDisplayBodyText)).perform(typeText(body));
        tryClickOnId(R.id.menuButtonImage);
        tryClickOnText(R.string.listAllNotes);
    }

    private void deleteNote(int indexNote) throws InterruptedException {
        Thread.sleep(500);
        onData(anything()).inAdapterView(withId(R.id.lvNotes))
                .atPosition(indexNote)
                .onChildView(withId(R.id.checkBox))
                .perform(click());
        tryClickOnId(R.id.btDelete);
    }

    private void restoreNote(int indexNote) throws InterruptedException {
        Thread.sleep(500);
        onData(anything()).inAdapterView(withId(R.id.lvNotes))
                .atPosition(indexNote)
                .onChildView(withId(R.id.checkBox))
                .perform(click());
        tryClickOnId(R.id.btRestore);
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
