package icynote.plugins;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.support.test.rule.ActivityTestRule;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import icynote.ui.BlankActivity;
import icynote.ui.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Kim Lan Phan Hoang
 */
public class PictureEditorTest {

    private BlankActivity mActivity;
    private Uri uri;
    private String absPath;

    @Rule
    public final ActivityTestRule<BlankActivity> main = new ActivityTestRule<>(BlankActivity.class);

    @Before
    public void setUp() throws Exception {
        mActivity = main.getActivity();
        // add image into the content resolver
        addImageInContentResolver();
        // start picture editor
        startPictureEditor();
    }

    private void startPictureEditor() throws InterruptedException {
        // start PictureEditor with uri and path in extra
        Intent i = new Intent(mActivity, PictureEditor.class);
        i.putExtra("uri", uri.toString());
        i.putExtra("absolutePath", absPath);
        mActivity.startActivity(i);
        Thread.sleep(500);
    }

    private void addImageInContentResolver() {

        // Create a Bitmap
        Bitmap bitmap = Bitmap.createBitmap(80, 100,Bitmap.Config.ARGB_8888);
        TextView tv = new TextView(mActivity);
        Canvas c = new Canvas(bitmap);
        tv.layout(0, 0, 80, 100);
        tv.draw(c);

        // save it into file
        File storageDir = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = null;
        try {
            file = File.createTempFile(
                    "name",
                    ".jpg",
                    storageDir
            );
            absPath = file.getAbsolutePath();
            FileOutputStream outputStream;
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        uri = Uri.fromFile(file);
    }

    @Test
    public void startingView() {
        onView(withId(R.id.preview_image)).check(matches(isDisplayed()));
        onView(withId(R.id.rotate_left)).check(matches(isDisplayed()));
        onView(withId(R.id.rotate_right)).check(matches(isDisplayed()));
        onView(withId(R.id.validate)).check(matches(isDisplayed()));
    }

    private Bitmap getBitmapFromFile() {
        InputStream inputStream;
        Bitmap b = null;
        try {
            inputStream = new FileInputStream(absPath);
            b = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }


    @Test
    public void rotateLeftTest() {
        Bitmap oldB = getBitmapFromFile();
        onView(withId(R.id.rotate_left)).perform(click());
        onView(withId(R.id.validate)).perform(click());
        Bitmap newB = getBitmapFromFile();
        assertEquals(oldB.getHeight(), newB.getWidth());
        assertFalse(oldB.sameAs(newB));
    }

    @Test
    public void rotateLeftAndRightTest() {
        Bitmap oldB = getBitmapFromFile();
        onView(withId(R.id.rotate_left)).perform(click());
        onView(withId(R.id.rotate_right)).perform(click());
        onView(withId(R.id.validate)).perform(click());
        Bitmap newB = getBitmapFromFile();
        assertTrue(oldB.sameAs(newB));
    }

    @Test
    public void rotateLeftTwiceTest() {
        Bitmap oldB = getBitmapFromFile();
        onView(withId(R.id.rotate_left)).perform(click(), click());
        onView(withId(R.id.validate)).perform(click());
        Bitmap newB = getBitmapFromFile();
        assertEquals(oldB.getWidth(), newB.getWidth());
    }

    @Test
    public void calculateInSampleSizeTest() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.outHeight = 1000;
        options.outWidth = 1500;

        int res = PictureEditor.calculateInSampleSize(options, 100, 100);
        assertEquals(10, res);
        int res2 = PictureEditor.calculateInSampleSize(options, 20, 20);
        assertEquals(50, res2);
    }

}

