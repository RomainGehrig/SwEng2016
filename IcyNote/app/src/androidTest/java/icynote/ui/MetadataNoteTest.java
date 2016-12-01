package icynote.ui;

import android.support.test.rule.ActivityTestRule;
import android.text.SpannableString;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.GregorianCalendar;
import java.util.concurrent.CountDownLatch;

import icynote.note.Note;
import icynote.note.impl.NoteData2;
import icynote.ui.fragments.MetadataNote;
import icynote.ui.view.MockMetadataNote;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;


public class MetadataNoteTest {
    @Rule
    public ActivityTestRule<MockMetadataNote> main = new ActivityTestRule<>(MockMetadataNote.class);

    private MockMetadataNote mActivity;
    private MetadataNote fragment;
    private Note<SpannableString> note;
    GregorianCalendar creation = new GregorianCalendar();


    @Before
    public void setUp() throws Exception {
        mActivity = main.getActivity();

        // instantiate fragment MetaData into MainActivity
        fragment = new MetadataNote();
        mActivity.openFragment(fragment);

        note = new NoteData2();
        note.setTitle(new SpannableString("someTitle"));
        note.setContent(new SpannableString("someContent"));
        note.setCreation(creation);
    }


    public void enableFragment() throws InterruptedException {
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
        onView(withId(R.id.noteTitle)).check(matches(withText(""+note.getTitle())));
    }

    /*@Test // FIXME toast failure ?
    public void onSaveNoteFailureTest() throws InterruptedException {
        enableFragment();
        fragment.receiveNote(note);
        fragment.onSaveNoteFailure("save error");
        onView(withId(R.id.noteTitle)).check(matches(withText(""+note.getTitle())));
    }*/

    @Test
    public void saveNoteWhenTypingTitleTest() throws InterruptedException {
        enableFragment();
        onView(withId(R.id.noteTitle)).perform(replaceText("a new title"));
        assertTrue(mActivity.saveNote);
    }

    /*@Test // FIXME
    public void getBackToNoteTest() throws InterruptedException {
        enableFragment();
        fragment.receiveNote(note);
        onView(withId(R.id.backButton)).perform(click());
        assertTrue(mActivity.openEditNote);
    }*/

    /*@Test // TODO to complete -> last to test to have almost 100% coverage
    public void receivePluginDataTest() throws InterruptedException {
        enableFragment();
        fragment.receiveNote(note);

        PluginsProvider pluginProvider = new PluginsProvider();
        fragment.receivePluginData(pluginProvider.getPlugins());
    }*/
}