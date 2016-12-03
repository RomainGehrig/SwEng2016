package icynote.ui;

import android.os.IBinder;
import android.support.test.espresso.Root;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.anything;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        MainActivity mActivity = main.getActivity();
    }


    // start app with notes list
    @Test
    public void startingViewTest() {
        onView(withId(R.id.menuButtonImage)).check(matches(isDisplayed()));
        onView(withId(R.id.searchBar)).check(matches(isDisplayed()));
        onView(withId(R.id.btAdd)).check(matches(isDisplayed()));
        onView(withId(R.id.btDelete)).check(matches(isDisplayed()));
    }

    @Test
    public void menuContainsAllButtonsTest() {
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).check(matches(isDisplayed()));
        onView(withText(R.string.trash)).check(matches(isDisplayed()));
        onView(withText(R.string.settings)).check(matches(isDisplayed()));
        onView(withText(R.string.logout)).check(matches(isDisplayed()));
    }

    // Open fragment tests
    @Test
    public void openFragmentListNotesTest() {
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());
        onView(withId(R.id.menuButtonImage)).check(matches(isDisplayed()));
        onView(withId(R.id.searchBar)).check(matches(isDisplayed()));
        onView(withId(R.id.btAdd)).check(matches(isDisplayed()));
        onView(withId(R.id.btDelete)).check(matches(isDisplayed()));
    }

    @Test
    public void openFragmentTrashTest() {
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.trash)).perform(click());
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
    public void openMetadataTest() {
        onView(withId(R.id.btAdd)).perform(click());
        onView(withId(R.id.note_open_metadata)).perform(click());
        onView(withId(R.id.backButton)).check(matches(isDisplayed()));
        onView(withId(R.id.noteTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.noteCreationDate)).check(matches(isDisplayed()));
        onView(withId(R.id.noteModificationDate)).check(matches(isDisplayed()));
    }

    @Test
    public void findNoteWithOneNoteTest() {
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());
        deleteAllNotes();
        addNote("note1", "body1");
        assertEquals(1, getNotesCount());
        onView(withId(R.id.searchBar)).perform(replaceText("note1-"));
        assertEquals(0, getNotesCount());
        onView(withId(R.id.searchBar)).perform(replaceText("note1"));
        assertEquals(1, getNotesCount());
    }

    @Test
    public void findNoteWithDeleteTest() {
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());
        deleteAllNotes();
        addNote("note1", "body1");
        addNote("note2", "body2");
        assertEquals(2, getNotesCount());
        onView(withId(R.id.searchBar)).perform(replaceText("note3"));
        assertEquals(0, getNotesCount());
        onView(withId(R.id.searchBar)).perform(replaceText("note"));
        assertEquals(2, getNotesCount());
    }

    @Test
    public void deletionTest()
    {
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());

        deleteAllNotes();
        assertEquals(0, getNotesCount());
        onView(withId(R.id.tvNumNotes)).check(matches(withText("0 notes")));

        addNote("note1", "body1");
        addNote("note2", "body2");
        addNote("note3", "body3");
        assertEquals(3, getNotesCount());
        onView(withId(R.id.tvNumNotes)).check(matches(withText("3 notes")));

        deleteNote(1);
        assertEquals(2, getNotesCount());
        onView(withId(R.id.tvNumNotes)).check(matches(withText("2 notes")));
        onView(withText("note2")).check(doesNotExist());
        onView(withText("note1")).check(matches(isDisplayed()));
        onView(withText("note3")).check(matches(isDisplayed()));
    }

    @Test
    public void reopenLastNoteWhenThereIsNoneTest()
    {
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());
        deleteAllNotes();
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.openLastNote)).perform(click());
        onView(withText("There is no last opened note.")).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void reopenLastNoteDoNotOpenDeletedNoteTest()
    {
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());
        deleteAllNotes();
        addNote("note1", "body1");
        deleteAllNotes();
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.openLastNote)).perform(click());
        onView(withText("There is no last opened note.")).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void reopenLastNoteOpenCorrectNoteTest()
    {
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());
        deleteAllNotes();
        addNote("note1", "body1");
        addNote("note2", "body2");
        addNote("note3", "body3");
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.openLastNote)).perform(click());

        onView(withId(R.id.noteDisplayTitleText)).check(matches(withText("note3")));
        onView(withId(R.id.noteDisplayBodyText)).check(matches(withText("body3")));
    }

    @Test
    public void titleModifiedInMetadataAppearsInListTest()
    {
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());
        deleteAllNotes();
        onView(withId(R.id.btAdd)).perform(click());
        onView(withId(R.id.note_open_metadata)).perform(click());
        onView(withId(R.id.noteTitle)).perform(replaceText("note1"));
        onView(withId(R.id.backButton)).perform(click());
        onView(withId(R.id.noteDisplayTitleText)).check(matches(withText("note1")));
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());
        assertEquals(1, getNotesCount());
    }

    @Test
    public void titleModifiedInEditNoteAppearsInMetadata()
    {
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());
        deleteAllNotes();
        onView(withId(R.id.btAdd)).perform(click());
        onView(withId(R.id.noteDisplayTitleText)).perform(replaceText("note1"));

        onView(withId(R.id.note_open_metadata)).perform(click());
        onView(withId(R.id.noteTitle)).check(matches(withText("note1")));
        onView(withId(R.id.backButton)).perform(click());
        onView(withId(R.id.noteDisplayTitleText)).check(matches(withText("note1")));
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());
        assertEquals(1, getNotesCount());
    }

    //---- Tests for the trash ------

    @Test
    public void findNoteWithOneNoteTrashTest() {
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
    }

    @Test
    public void findNoteWithDeleteTrashTest() {
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());
        addNote("note1", "body1");
        addNote("note2", "body2");
        deleteNote(0);
        deleteNote(0);

        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.trash)).perform(click());
        assertEquals(2, getNotesCount());
        onView(withId(R.id.searchBar)).perform(replaceText("note3"));
        assertEquals(0, getNotesCount());
        onView(withId(R.id.searchBar)).perform(replaceText("note"));
        assertEquals(2, getNotesCount());
    }

    @Test
    public void deletingANoteAddItToTheTrashTest() {
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.trash)).perform(click());
        assertEquals(0, getNotesCount());

        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());
        addNote("note1", "body1");
        deleteNote(0);

        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.trash)).perform(click());
        assertEquals(1, getNotesCount());
    }

    @Test
    public void deletingThreeNotesAddThemToTheTrashTest() {
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.trash)).perform(click());
        assertEquals(0, getNotesCount());

        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());
        addNote("note1", "body1");
        addNote("note2", "body2");
        addNote("note3", "body3");
        deleteNote(0);
        deleteNote(0);
        deleteNote(0);

        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.trash)).perform(click());
        assertEquals(3, getNotesCount());
    }

    @Test
    public void restoringANoteTest() {
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());
        int numberOfNotes = getNotesCount();
        addNote("note1", "body1");
        assertEquals(1 + numberOfNotes, getNotesCount());
        deleteNote(0);
        assertEquals(numberOfNotes, getNotesCount());

        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.trash)).perform(click());
        assertEquals(1, getNotesCount());
        restoreNote(0);
        assertEquals(0, getNotesCount());

        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());
        assertEquals(1 + numberOfNotes, getNotesCount());
    }

    @Test
    public void restoringThreeNotesTest() {
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());
        int numberOfNotes = getNotesCount();
        addNote("note1", "body1");
        addNote("note2", "body2");
        addNote("note3", "body3");
        assertEquals(3+numberOfNotes, getNotesCount());
        deleteNote(0);
        deleteNote(0);
        deleteNote(0);
        assertEquals(numberOfNotes, getNotesCount());

        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.trash)).perform(click());
        assertEquals(3, getNotesCount());
        onData(anything()).inAdapterView(withId(R.id.lvNotes))
                .atPosition(0)
                .onChildView(withId(R.id.checkBox))
                .perform(click());
        onData(anything()).inAdapterView(withId(R.id.lvNotes))
                .atPosition(1)
                .onChildView(withId(R.id.checkBox))
                .perform(click());
        onData(anything()).inAdapterView(withId(R.id.lvNotes))
                .atPosition(2)
                .onChildView(withId(R.id.checkBox))
                .perform(click());
        onView(withId(R.id.btRestore)).perform(click());
        assertEquals(0, getNotesCount());

        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());
        assertEquals(3+numberOfNotes, getNotesCount());
    }


    //---- Helper methods -----------

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

    public int getNotesCount() {
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
}
