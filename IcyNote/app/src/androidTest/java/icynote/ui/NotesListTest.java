package icynote.ui;

import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.UiThreadTestRule;
import android.text.SpannableString;
import android.view.View;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import icynote.note.Note;
import icynote.note.impl.NoteData2;
import icynote.ui.fragments.NotesList;
import icynote.ui.view.MockNotesList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Kim Lan
 */
public class NotesListTest  {

    @Rule
    public ActivityTestRule<MockNotesList> main = new ActivityTestRule<>(MockNotesList.class);

    @Rule
    public UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

    private MockNotesList mActivity;
    private NotesList fragment;
    private List<Note<SpannableString>> list = new ArrayList<>();


    @Before
    public void setUp() throws Exception {
        mActivity = main.getActivity();

        // instantiate fragment EditNote into MainActivity
        fragment = new NotesList();

        mActivity.openFragment(fragment);

        // ListView
        ListView lv = (ListView) (mActivity.findViewById(R.id.lvNotes));

        // add notes to list
        NoteData2 n = new NoteData2();
        n.setContent(new SpannableString("hey"));
        n.setTitle(new SpannableString("title"));
        list.add(n);
    }

    @Test
    public void receiveNotesTest() throws InterruptedException {
        enableFragment();
        int notesCount = getNotesCount();
        int countExpected = list.size();
        assertEquals(countExpected, notesCount);
        NoteData2 n2 = new NoteData2();
        n2.setContent(new SpannableString("hey2"));
        n2.setTitle(new SpannableString("title2"));
        list.add(n2);
        enableFragment();
        notesCount = getNotesCount();
        countExpected = list.size();
        assertEquals(countExpected, notesCount);
        NoteData2 n3 = new NoteData2();
        n3.setContent(new SpannableString("hey3"));
        n3.setTitle(new SpannableString("title3"));
        list.add(n3);
        enableFragment();
        notesCount = getNotesCount();
        countExpected = list.size();
        assertEquals(countExpected, notesCount);
    }

    @Test
    public void createNoteTest() throws InterruptedException {
        enableFragment();
        onView(withId(R.id.btAdd)).perform(click());
        assertTrue(mActivity.createNote);
    }

    /*@Test FIXME
    public void openNoteTest() throws InterruptedException {
        enableFragment();
        onView(withId(R.id.item_content)).perform(click());
        assertTrue(mActivity.openNote);
    }*/

    @Test
    public void deleteNoteTest() throws InterruptedException {
        enableFragment();

        int notesCountBefore = getNotesCount();
        assertEquals(1, notesCountBefore);

        onView(withId(R.id.checkBox)).perform(click());

        onView(withId(R.id.btDelete)).perform(click());// TODO check it is the right delete button
        assertTrue(mActivity.deleteNote);

        // Méthode 1
        /*onView(isNotChecked()).perform(click());*/

        // Méthode 2 : Fonctionne mais ne permet pas de sélectionner une checkbox spécifique
        // onView(withId(R.id.checkBox)).perform(click());

        // Méthode 3
        /*onData(withId(R.id.lvNotes)).inAdapterView(withId(R.id.checkBox)).atPosition(0)
                .perform(click());*/

        // Méthode 4 : si lv n'est pas nul :
        /*
        View view = getViewByPosition(0, lv);
        view.findViewById(R.id.checkBox);
        onView(withId(R.id.btDelete)).perform(click());
        //onView(withId(R.id.lvNotes))
        */

        // Méthode 5 : récupérer la listView d'une autre manière
        //checkBoxTester();
    }

    @Test
    public void deleteNoteWhenNotCheckedTest() throws InterruptedException {
        enableFragment();

        int notesCountBefore = getNotesCount();
        assertEquals(1, notesCountBefore);

        onView(withId(R.id.btDelete)).perform(click());// TODO check it is the right delete button
        assertFalse(mActivity.deleteNote);
    }

    @Test
    public void checkNoteMultipleTimeDeleteTest() throws InterruptedException {
        enableFragment();

        int notesCountBefore = getNotesCount();
        assertEquals(1, notesCountBefore);

        onView(withId(R.id.btDelete)).perform(click());
        assertFalse(mActivity.deleteNote);

        onView(withId(R.id.checkBox)).perform(click());
        onView(withId(R.id.checkBox)).perform(click());
        onView(withId(R.id.btDelete)).perform(click());
        assertFalse(mActivity.deleteNote);

        onView(withId(R.id.checkBox)).perform(click());
        onView(withId(R.id.checkBox)).perform(click());
        onView(withId(R.id.checkBox)).perform(click());
        onView(withId(R.id.btDelete)).perform(click());
        assertTrue(mActivity.deleteNote);
    }

    /*@Test
    public void onOpenNoteFailureTest() throws InterruptedException {
        enableFragment();
        fragment.onOpenNoteFailure("open fail");

        onView(withId(R.id.lvNotes)).check(matches(isDisplayed()));
        onView(withId(R.id.checkBox)).check(matches(isDisplayed()));
        onView(withId(R.id.btAdd)).check(matches(isDisplayed()));
        onView(withId(R.id.btDelete)).check(matches(isDisplayed()));
    }

    @Test
    public void onCreateNoteFailureTest() throws InterruptedException {
        enableFragment();
        fragment.onCreateNoteFailure("create fail");

        onView(withId(R.id.lvNotes)).check(matches(isDisplayed()));
        onView(withId(R.id.checkBox)).check(matches(isDisplayed()));
        onView(withId(R.id.btAdd)).check(matches(isDisplayed()));
        onView(withId(R.id.btDelete)).check(matches(isDisplayed()));
    }

    @Test
    public void onDeleteNoteFailureTest() throws InterruptedException {
        enableFragment();
        fragment.onNoteDeletionFailure(list.get(0), "delete fail");

        onView(withId(R.id.lvNotes)).check(matches(isDisplayed()));
        onView(withId(R.id.checkBox)).check(matches(isDisplayed()));
        onView(withId(R.id.btAdd)).check(matches(isDisplayed()));
        onView(withId(R.id.btDelete)).check(matches(isDisplayed()));
    }*/

    //-----------------------------------------------------------------------

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
                        fragment.receiveNotes(list);
                    }// end of run
                } // end of runnable
        ); //end of runOnUiThread

        // dans le thread du test, attend qu elle passe a 0 = attend que receiveNote soit appele
        latch.await();
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    /*public int checkBoxTester()
    {
        final int[] counts = new int[1];
        onView(withId(R.id.lvNotes)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                ListView listView = (ListView) view;

                View v = getViewByPosition(0, listView);
                v.findViewById(R.id.checkBox);
                onView(withId(R.id.btDelete)).perform(click());
                int notesCountAfter = getNotesCount();
                assertEquals(0, notesCountAfter);


                counts[0] = listView.getCount();

                return true;
            }

            @Override
            public void describeTo(Description description) {

            }
        }));

        return counts[0];
    }*/

    public int getNotesCount()
    {
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