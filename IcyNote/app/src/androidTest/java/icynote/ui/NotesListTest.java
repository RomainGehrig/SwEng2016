package icynote.ui;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.Test;

import icynote.core.Note;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by kl on 09.11.2016.
 */
@RunWith(AndroidJUnit4.class)
public class NotesListTest extends ActivityInstrumentationTestCase2<BlankActivity> {

    private BlankActivity mActivity;

    public NotesListTest() {
        super(BlankActivity.class);
    }


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        final Context context = InstrumentationRegistry.getTargetContext();
        mActivity = getActivity();

        // instanciate fragment EditNote into MainActivity
        Fragment fragment = (Fragment) NotesList.class.newInstance();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).commit();

    }

    @Test
    public void test() {

    }

}