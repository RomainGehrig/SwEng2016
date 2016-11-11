package icynote.ui;

import static icynote.ui.NotesList.notes;
import static org.mockito.Mockito.*;

import android.content.Context;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.junit.Test;


import dalvik.annotation.TestTargetClass;
import icynote.core.IcyNoteCore;
import icynote.core.Note;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by kl on 09.11.2016.
 */
@RunWith(AndroidJUnit4.class)
public class EditNoteTest extends ActivityInstrumentationTestCase2<BlankActivity> {

    /********** DIRTY HACK FOR TEST *********/
    Note note;

    private BlankActivity mActivity;

    public EditNoteTest() {
        super(BlankActivity.class);
    }


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        final Context context = InstrumentationRegistry.getTargetContext();
        mActivity = getActivity();

        Bundle bundle = new Bundle();
        bundle.putInt("id",0);

        note = mock(Note.class);
        notes.add(note);

        // instanciate fragment EditNote into MainActivity
        Fragment fragment = (Fragment) EditNote.class.newInstance();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).commit();

    }

    /*@Test
    public void goToMetadataTest() {
        onView(withId(R.id.noteDisplaySettingsButton)).perform(click());
        assertNotNull(mActivity.findViewById(R.id.noteTitle));
    }*/

    @Test
    public void writeTitleTest() {
        onView(withId(R.id.noteDisplayTitleText)).perform(typeText("someTitle"));
        verify(note).setTitle("someTitle");
    }

    @Test
    public void writeContentTest() {
        onView(withId(R.id.noteDisplayBodyText)).perform(typeText("someText"));
        verify(note).setContent("someText");
    }

}