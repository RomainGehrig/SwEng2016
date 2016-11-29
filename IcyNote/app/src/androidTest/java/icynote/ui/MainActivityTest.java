package icynote.ui;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class MainActivityTest {

    private MainActivity mActivity;

    @Rule
    public ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        mActivity = main.getActivity();
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

}
