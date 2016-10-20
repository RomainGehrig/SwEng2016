package icynote.ui;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.project.R;

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
    public void openFragmentTest() {

       // NavigationView menu = (NavigationView) mActivity.findViewById(R.id.menu);

        onView(withId(R.id.menuButton)).perform(click());
        onView(withText(R.string.listAllNotes)).perform(click());
        assertNotNull(mActivity.findViewById(R.id.fragmentNotesList));

        onView(withId(R.id.menuButton)).perform(click());
        onView(withText(R.string.editTags)).perform(click());
        assertNotNull(mActivity.findViewById(R.id.fragmentEditTags));

        /*
        onView(withId(R.id.menuButton)).perform(click());
        onView(withText(R.string.trash)).perform(click());
        assertNotNull(mActivity.findViewById(R.id.fragmentTrash));
        */

        onView(withId(R.id.menuButton)).perform(click());
        onView(withText(R.string.settings)).perform(click());
        assertNotNull(mActivity.findViewById(R.id.fragmentSettings));

        onView(withId(R.id.menuButton)).perform(click());
        onView(withText(R.string.logout)).perform(click());
        assertNotNull(mActivity.findViewById(R.id.fragmentLogout));

    }
}