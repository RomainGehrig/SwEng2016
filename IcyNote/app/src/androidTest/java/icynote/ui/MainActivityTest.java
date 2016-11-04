package icynote.ui;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


/**
 * Created by kl on 20.10.2016.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mActivity;

    public MainActivityTest() {
        super(MainActivity.class);
    }


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        final Context context = InstrumentationRegistry.getTargetContext();
        mActivity = getActivity();
    }

    /*
    private Fragment startFragment(Fragment fragment) {
        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        //transaction.add(R.id.content_frame, fragment, "tag");
        //transaction.commit();
        getInstrumentation().waitForIdleSync();
        Fragment frag = mActivity.getSupportFragmentManager().findFragmentById(R.id.content_frame);
        return frag;
    }
    */

    @Test
    public void startingViewTest() {
        assertNotNull(mActivity.findViewById(R.id.noteDisplayTitleText));
        assertNotNull(mActivity.findViewById(R.id.note_open_metadata));
        assertNotNull(mActivity.findViewById(R.id.noteDisplaySettingsButton));
        assertNotNull(mActivity.findViewById(R.id.noteDisplayBodyText));
        assertNotNull(mActivity.findViewById(R.id.noteDisplayTagsText));
        assertNotNull(mActivity.findViewById(R.id.menuButton));
        assertNotNull(mActivity.findViewById(R.id.menuButtonImage));
    }

    @Test
    public void openFragmentListNotesTest() {
        assertNull(mActivity.findViewById(R.id.listNotesView));
        onView(withId(R.id.menuButton)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());
        assertNotNull(mActivity.findViewById(R.id.listNotesView));
        assertNotNull(mActivity.findViewById(R.id.menuButton));
        assertNotNull(mActivity.findViewById(R.id.menuButtonImage));
    }

    @Test
    public void openFragmentEditTagsTest() {
        assertNull(mActivity.findViewById(R.id.editTagsView));
        onView(withId(R.id.menuButton)).perform(click());
        onView(withText(R.string.editTags)).perform(click());
        assertNotNull(mActivity.findViewById(R.id.editTagsView));
        assertNotNull(mActivity.findViewById(R.id.menuButton));
        assertNotNull(mActivity.findViewById(R.id.menuButtonImage));
    }

    @Test
    public void openFragmentTrashTest() {

        onView(withId(R.id.menuButton)).perform(click());
        onView(withText(R.string.trash)).perform(click());
        assertNotNull(mActivity.findViewById(R.id.noteDisplayTitleText));
        assertNotNull(mActivity.findViewById(R.id.note_open_metadata));
        assertNotNull(mActivity.findViewById(R.id.noteDisplaySettingsButton));
        assertNotNull(mActivity.findViewById(R.id.noteDisplayBodyText));
        assertNotNull(mActivity.findViewById(R.id.noteDisplayTagsText));
        assertNotNull(mActivity.findViewById(R.id.menuButton));
        assertNotNull(mActivity.findViewById(R.id.menuButtonImage));
    }

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

    @Test
    public void openFragmentLogoutTest() {
        assertNull(mActivity.findViewById(R.id.logoutView));
        onView(withId(R.id.menuButton)).perform(click());
        onView(withText(R.string.logout)).perform(click());
        assertNotNull(mActivity.findViewById(R.id.logoutView));
        assertNotNull(mActivity.findViewById(R.id.menuButton));
        assertNotNull(mActivity.findViewById(R.id.menuButtonImage));
    }

    @Test
    public void openFragmentMetadatasTest() {
        assertNull(mActivity.findViewById(R.id.noteTitle));
        assertNull(mActivity.findViewById(R.id.noteCreationDate));
        assertNull(mActivity.findViewById(R.id.checkBox1));
        assertNull(mActivity.findViewById(R.id.checkBox2));
        assertNull(mActivity.findViewById(R.id.checkBox3));
        assertNull(mActivity.findViewById(R.id.backButton));
        onView(withId(R.id.noteDisplaySettingsButton)).perform(click());
        assertNotNull(mActivity.findViewById(R.id.noteTitle));
        assertNotNull(mActivity.findViewById(R.id.noteCreationDate));
        assertNotNull(mActivity.findViewById(R.id.checkBox1));
        assertNotNull(mActivity.findViewById(R.id.checkBox2));
        assertNotNull(mActivity.findViewById(R.id.checkBox3));
        assertNotNull(mActivity.findViewById(R.id.backButton));
    }
}