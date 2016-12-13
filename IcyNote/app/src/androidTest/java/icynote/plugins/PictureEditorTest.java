package icynote.plugins;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.support.test.rule.ActivityTestRule;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import icynote.ui.BlankActivity;
import icynote.ui.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by kl on 13.12.2016.
 */
public class PictureEditorTest {

    private BlankActivity mActivity;
    private Uri uri;
    private String absPath;
    private Bitmap bitmap;

    @Rule
    public final ActivityTestRule<BlankActivity> main = new ActivityTestRule<>(BlankActivity.class);

    @Before
    public void setUp() throws Exception {
        mActivity = main.getActivity();
        addImageInContentResolver();

        // start PictureEditor with uri and path in extra
        Intent i = new Intent(mActivity, PictureEditor.class);
        i.putExtra("uri", uri.toString());
        i.putExtra("absolutePath", absPath);
        mActivity.startActivity(i);

    }

    private void addImageInContentResolver() {

        // Create a Bitmap
        bitmap = Bitmap.createBitmap(80, 100,Bitmap.Config.ARGB_8888);
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


    private void startingView() {
        onView(withId(R.id.preview_image)).check(matches(isDisplayed()));
        onView(withId(R.id.rotate_left)).check(matches(isDisplayed()));
        onView(withId(R.id.rotate_right)).check(matches(isDisplayed()));
        onView(withId(R.id.validate)).check(matches(isDisplayed()));
    }

    @Test
    public void rotateTest() {
        onView(withId(R.id.rotate_left)).perform(click());
        onView(withId(R.id.rotate_right)).perform(click());

        // the view is still there
        startingView();

        // TODO check rotate is correct (check size)
    }
}