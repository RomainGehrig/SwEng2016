
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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Kim Lan
 */
/*
public class NotesListTest  {

    @Rule
    public ActivityTestRule<MockNotesList> main = new ActivityTestRule<>(MockNotesList.class);

    @Rule
    public UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

    private MockNotesList mActivity;
    private NotesList fragment;
    private List<Note<SpannableString>> list = new ArrayList<>();
    private ListView lv;


    @Before
    public void setUp() throws Exception {
        mActivity = main.getActivity();

        // instantiate fragment EditNote into MainActivity
        fragment = new NotesList();

        mActivity.openFragment(fragment);

        // ListView
        lv = (ListView)(mActivity.findViewById(R.id.lvNotes));

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
    }

    @Test
    public void createNoteTest() throws InterruptedException {
        enableFragment();
        onView(withId(R.id.btAdd)).perform(click());
        assertTrue(mActivity.createNote);
    }

    @Test
    public void openNoteTest() throws InterruptedException {
        enableFragment();
        onView(withId(R.id.item_content)).perform(click());
        assertTrue(mActivity.openNote);
    }

    @Test
    public void deleteNoteTest() throws InterruptedException {
        enableFragment();
        int notesCountBefore = getNotesCount();
        assertEquals(1, notesCountBefore);
        //NotesAdapter adapter = (NotesAdapter)lv.getAdapter();
        //adapter.getItemId(0);
        View view = getViewByPosition(0, lv);
        view.findViewById(R.id.checkBox);
        onView(withId(R.id.btDelete)).perform(click());
        //onView(withId(R.id.lvNotes))
        //onView(withId(R.id.btDelete)).perform(click());// TODO check it is the right delete button
        int notesCountAfter = getNotesCount();
        assertEquals(0, notesCountAfter);
    }

    @Test
    public void onOpenNoteFailureTest() {
        fragment.receiveNotes(list);
        fragment.onOpenNoteFailure("open fail");

        // check the list is still here
        assertEquals(lv.getAdapter().getCount(), list.size());
    }

    @Test
    public void onCreateNoteFailureTest() {
        fragment.receiveNotes(list);
        fragment.onOpenNoteFailure("create fail");

        // check the list is still here
        assertEquals(lv.getAdapter().getCount(), list.size());
    }

    @Test
    public void onDeleteNoteFailureTest() {
        fragment.receiveNotes(list);
        fragment.onOpenNoteFailure("delete fail");

        // check the list is still here
        assertEquals(lv.getAdapter().getCount(), list.size());
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
}*/
