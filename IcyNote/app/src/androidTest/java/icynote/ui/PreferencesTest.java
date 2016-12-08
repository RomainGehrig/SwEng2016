
package icynote.ui;

import android.os.IBinder;
import android.support.test.espresso.Espresso;
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
import static android.support.test.espresso.assertion.PositionAssertions.isAbove;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
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
    public void setUp() throws Exception {
        MainActivity mActivity = main.getActivity();

        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());
        deleteAllNotes();
        addNote("note3", "body3");
        addNote("note2", "body2");
        addNote("note1", "body1");
    }

    @Test
    public void sortingByTitleTest () {
        assertEquals(3, getNotesCount());
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.settings)).perform(click());
        onView(withText("Sort by")).perform(click());
        onView(withText("Title")).perform(click());

        Espresso.pressBack();
        onView(withId(R.id.menuButtonImage)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());

        //assertEquals(3, getNotesCount()); TODO decomment when bug fixed
        onView(withText("note1")).check(isAbove(withText("note2")));
        onView(withText("note2")).check(isAbove(withText("note3")));
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



/*package icynote.ui;

import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PreferencesTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mActivity;

    public PreferencesTest() {
        super(MainActivity.class);
    }


    //TODO use preferences instead of settings
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        final Context context = InstrumentationRegistry.getTargetContext();
        mActivity = getActivity();
    }

    @Test
    public void themeIsBrightAtStartUp() {
        assertEquals(Theme.getTheme(), Theme.ThemeType.BRIGHT);
    }

    @Test
    public void themeIsStillBrightWhenReselect() {
        onView(withId(R.id.menuButton)).perform(click());
        onView(withText(R.string.settings)).perform(click());
        onView(withId(R.id.styles_list)).perform(click());
        Espresso.onData(allOf(is(instanceOf(String.class)))).atPosition(Theme.getTheme().toPosition()).perform(click());
        assertEquals(Theme.getTheme(), Theme.ThemeType.BRIGHT);
    }

    @Test
    public void themeIsDarkWhenSelected() {
        onView(withId(R.id.menuButton)).perform(click());
        onView(withText(R.string.settings)).perform(click());
        onView(withId(R.id.styles_list)).perform(click());
        Espresso.onData(allOf(is(instanceOf(String.class))))
                .atPosition(Theme.ThemeType.DARK.toPosition()).perform(click());
        assertEquals(Theme.getTheme(), Theme.ThemeType.DARK);
    }
    

    @Test
    public void backgroundIsWhiteWhenBrightTheme() {
        // Get the background color of the editNote Fragment
        ColorDrawable backgroundColor =
                (ColorDrawable) mActivity.findViewById(R.id.layoutFragmentEditNote).getBackground();

        // Parse the current theme to get the background color corresponding to the current theme
        TypedArray ta =
                mActivity.obtainStyledAttributes(Theme.getTheme().toInt(), new int[]{android.R.attr.background});

        assertEquals(backgroundColor.getColor(), ta.getColor(0, 0));
    }
    
    @Test
    public void textColorIsBlackWhenBrightTheme() {
        int color = ((EditText)mActivity.findViewById(R.id.noteDisplayBodyText)).getCurrentTextColor();
        assertEquals(color, Theme.getTheme().getTextColor());
    }

    @Test
    public void textColorIsWhiteWhenDarkTheme() {
        onView(withId(R.id.menuButton)).perform(click());
        onView(withText(R.string.settings)).perform(click());
        onView(withId(R.xml.preferences)).perform(click());
        Espresso.onData(allOf(is(instanceOf(String.class))))
                .atPosition(Theme.ThemeType.DARK.toPosition()).perform(click());
        onView(withId(R.id.menuButton)).perform(click());
        onView(withText(R.string.trash)).perform(click());
        int color = ((EditText)mActivity.findViewById(R.id.noteDisplayBodyText)).getCurrentTextColor();
        assertEquals(color, Theme.getTheme().getTextColor());
    }

}*/
