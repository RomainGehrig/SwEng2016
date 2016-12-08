package icynote.ui;

import android.support.test.rule.ActivityTestRule;
import android.text.SpannableString;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import icynote.note.Note;
import icynote.note.impl.NoteData2;
import icynote.ui.fragments.EditNote;
import icynote.ui.view.MockEditNote;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;

public class EditNoteTest {

    @Rule
    public final ActivityTestRule<MockEditNote> main = new ActivityTestRule<>(MockEditNote.class);

    private MockEditNote mActivity;
    private EditNote fragment;
    private Note<SpannableString> note;


    @Before
    public void setUp() throws Exception {
        mActivity = main.getActivity();

        // instantiate fragment EditNote into MainActivity
        fragment = new EditNote();
        mActivity.openFragment(fragment);

        note = new NoteData2();
        note.setTitle(new SpannableString("someTitle"));
        note.setContent(new SpannableString("someContent"));
    }


    private void enableFragment() throws InterruptedException {
        //initalise a 1 = se debloque apres 1 countDown
        final CountDownLatch latch = new CountDownLatch(1);
        mActivity.runOnUiThread(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        latch.countDown(); // fait passer a 0
                        fragment.receiveNote(note);
                    }// end of run
                } // end of runnable
        ); //end of runOnUiThread

        // dans le thread du test, attend qu elle passe a 0 = attend que receiveNote soit appele
        latch.await();
    }

    @Test
    public void receiveNoteTest() throws InterruptedException {
        enableFragment();
        fragment.receiveNote(note);
        onView(withId(R.id.noteDisplayTitleText)).check(matches(withText("someTitle")));
        onView(withId(R.id.noteDisplayBodyText)).check(matches(withText("someContent")));
    }

    /*@Test
    public void goToMetadataTest() throws InterruptedException {
        enableFragment();
        fragment.receiveNote(note);
        onView(withId(R.id.note_open_metadata)).perform(click());
        // assertTrue(mActivity.openMetadata); // FIXME
    }*/

   /* @Test // FIXME
    public void writeTitleTest() throws InterruptedException {
        enableFragment();
        fragment.receiveNote(note);
        onView(withId(R.id.noteDisplayTitleText)).perform(replaceText("someTitle"));
        assertTrue(mActivity.saveNote);
    }*/

    /*@Test
    public void writeContentTest() throws InterruptedException {
        enableFragment();
        fragment.receiveNote(note);
        onView(withId(R.id.noteDisplayBodyText)).perform(replaceText("someText"));
        assertTrue(mActivity.saveNote);
    }*/

    /*@Test
    public void saveNoteTest() throws InterruptedException { // TODO more checks
        enableFragment();
        mActivity.saveNote(note, EditNoteTest.this);
        onView(withId(R.id.noteDisplayTitleText)).check(matches(withText("someTitle")));
        onView(withId(R.id.noteDisplayBodyText)).check(matches(withText("someContent")));
    }*/

    @Test
    public void onSaveNoteFailureTest() {
        fragment.receiveNote(note);
        fragment.onSaveNoteFailure("fail to save");
        onView(withId(R.id.noteDisplayTitleText)).check(matches(withText("someTitle")));
        onView(withId(R.id.noteDisplayBodyText)).check(matches(withText("someContent")));
    }
    
}