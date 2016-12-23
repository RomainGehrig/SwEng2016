package icynote.exporters;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.text.SpannableString;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import icynote.note.Note;
import icynote.note.impl.NoteData;
import icynote.plugins.ImageFormatter;
import icynote.ui.MainActivity;

import static org.junit.Assert.assertNotEquals;

public class HTMLExporterTest {

    private static final String NON_EXISTING_IMAGE_URI = "I_DO_NOT_EXIST";
    private Activity mActivity;

    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws InterruptedException {
        boolean tryAgain = true;
        int tryNb = 0;
        while (tryAgain && tryNb < 20) {
            tryAgain = false;
            try {
                mActivity = main.getActivity();
            } catch (RuntimeException e) {
                tryAgain = true;
                tryNb += 1;
                Thread.sleep(500);
            }
        }
    }

    private Note<SpannableString> createNote() {
        SpannableString title = new SpannableString("This is a title");
        SpannableString content = new SpannableString("This is some content");
        return new NoteData<>(title, content);
    }

    private void attachNonExistingImageUri(Note<SpannableString> note) {
        SpannableString content = note.getContent();
        content.setSpan(ImageFormatter.getImageSpan(mActivity.getResources(), null, NON_EXISTING_IMAGE_URI),
                content.length(),
                content.length(),
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
    }

    @Test
    public void canExportSimpleNote() {
        Note<SpannableString> note = createNote();
        HTMLExporter.HTMLNote htmlNote = new HTMLExporter().export(note, mActivity.getApplicationContext());
        assertNotEquals(0, htmlNote.getBytes().length);
    }

    @Test
    public void doesNotCrashOnInexistingImage() {
        Note<SpannableString> note = createNote();
        attachNonExistingImageUri(note);
        HTMLExporter.HTMLNote htmlNote = new HTMLExporter().export(note, mActivity.getApplicationContext());
        assertNotEquals(0, htmlNote.getBytes().length);
    }
}
