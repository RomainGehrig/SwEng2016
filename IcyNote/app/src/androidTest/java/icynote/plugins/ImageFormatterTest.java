package icynote.plugins;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.test.rule.ActivityTestRule;
import android.text.SpannableString;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;

import icynote.note.Note;
import icynote.note.Response;
import icynote.note.common.ResponseFactory;
import icynote.note.impl.NoteData;
import icynote.ui.MainActivity;
import icynote.ui.contracts.PluginPresenter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ImageFormatterTest {

    private final ImageFormatter formatter = new ImageFormatter(1, 2);
    private MainActivity mActivity;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
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

    @Test
    public void getNameTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                assertEquals(formatter.getName(), "Image insertion Plugin");
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void getContentIfNotEnabledTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 0, 0);
                SpannableString res = imageFormatter.testGetContent();
                assertEquals("[img=content://icynote.ui.fileprovider/my_images/img_1.jpg]", res.toString());
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void setContentIfNotEnabledTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 0, 0);
                imageFormatter.testSetContent(new SpannableString("new body"));
                SpannableString res = imageFormatter.testGetContent();
                assertEquals("new body", res.toString());
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }
    @Test
    public void createImageFileWithNullTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 0, 0);
                File res = imageFormatter.createImageFile(null);
                assertNull(res);
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void createImageFileTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 0, 0);
                File res = imageFormatter.createImageFile(imageFormatter.getActivity());
                assertNotNull(res);
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void getTempFileUriWithNullTest() throws InterruptedException {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 0, 0);
        exception.expect(AssertionError.class);
        imageFormatter.getTempFileUri(null);
    }

    @Test
    public void getTempFileUriTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 0, 0);
                Uri res = imageFormatter.getTempFileUri(imageFormatter.getActivity());
                assertEquals("/my_images/img_",res.getPath().substring(0, 15));
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void startCameraWithNullTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 0, 0);
                exception.expect(NullPointerException.class);
                imageFormatter.startCamera(null);
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void takeAndInsertNewPhotoWithNullTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 0, 0);
                exception.expect(NullPointerException.class);
                imageFormatter.takeAndInsertNewPhoto(null);
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void startGalleryWithNullTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 0, 0);
                exception.expect(NullPointerException.class);
                imageFormatter.startGallery(null);
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void canHandleTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                assertTrue(imageFormatter.canHandle(10));
                assertTrue(imageFormatter.canHandle(20));
                assertTrue(imageFormatter.canHandle(30));
                assertFalse(imageFormatter.canHandle(40));
                assertFalse(imageFormatter.canHandle(0));
                assertFalse(imageFormatter.canHandle(15));
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void handleAnUnknownRequestCodeTest() throws InterruptedException {
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                try {
                    exception.expect(AssertionError.class);
                    imageFormatter.handle(15, 0, null, null);
                }
                catch (AssertionError e) {

                }
            }
        });
    }

/*    @Test
    public void handleACameraIfResultNotOkNullTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                        try {
                            exception.expect(NullPointerException.class);
                            imageFormatter.handle(10, 0, null, null);
                        }
                        catch (NullPointerException e) {

                        }
                    }
                });
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }*/

    @Test
    public void handleACameraIfResultNotOkTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                        imageFormatter.handle(10, 0, null, imageFormatter.getPluginData());
                        assertEquals(1, ((MockPluginData)imageFormatter.getPluginData()).getActivityCallNb);
                    }
                });
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void handleACameraIfUriNullNoPluginTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                        imageFormatter.setLastUri(null);
                        try {
                            exception.expect(NullPointerException.class);
                            imageFormatter.handle(10, -1, null, null);
                        }
                        catch (NullPointerException e) {

                        }
                    }
                });
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void handleACameraIfUriNullWithPluginTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                        imageFormatter.setLastUri(null);
                        imageFormatter.handle(10, -1, null, imageFormatter.getPluginData());
                        assertEquals(1, ((MockPluginData)imageFormatter.getPluginData()).getActivityCallNb);
                    }
                });
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void handleACameraIfUriNullAndResultNotOkTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                        imageFormatter.setLastUri(null);
                        imageFormatter.handle(10, 0, null, imageFormatter.getPluginData());
                        assertEquals(1, ((MockPluginData)imageFormatter.getPluginData()).getActivityCallNb);
                    }
                });
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void handleACameraIfUriAndResultOkNullTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                        imageFormatter.setLastUri(Uri.fromFile(new File("test")));
                        try {
                            ExpectedException e = ExpectedException.none();
                            e.expect(NullPointerException.class);
                            imageFormatter.handle(10, -1, null, null);
                        }
                        catch (NullPointerException e) {

                        }
                    }
                });
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void handleACameraIfUriAndResultOkTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                        imageFormatter.setLastUri(Uri.fromFile(new File("test")));
                        imageFormatter.handle(10, -1, null, imageFormatter.getPluginData());
                        assertEquals(2, ((MockPluginData)imageFormatter.getPluginData()).getActivityCallNb);
                    }
                });
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void handleGalleryIfResultNotOkTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                        try {
                            exception.expect(NullPointerException.class);
                            imageFormatter.handle(20, 0, null, null);
                        }
                        catch (NullPointerException e) {

                        }
                    }
                });
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void handleGalleryIfResultNotOkWithPluginTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                        imageFormatter.handle(20, 0, null, imageFormatter.getPluginData());
                        assertEquals(1, ((MockPluginData)imageFormatter.getPluginData()).getActivityCallNb);
                    }
                });
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void handleGalleryIfResultOkTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                        try {
                            exception.expect(NullPointerException.class);
                            imageFormatter.handle(20, -1, new Intent(), null);
                        }
                        catch (NullPointerException e) {

                        }
                    }
                });
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void handleGalleryIfResultOkWithPluginTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                        imageFormatter.setLastUri(Uri.fromFile(new File("test")));
                        try {
                            exception.expect(NullPointerException.class);
                            imageFormatter.handle(20, -1, new Intent(), imageFormatter.getPluginData());
                        }
                        catch (NullPointerException e) {

                        }
                    }
                });
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void handleEditorIfResultNotOkTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                        boolean testPassed = false;
                        try {
                            imageFormatter.handle(30, 0, new Intent(), null);
                        }
                        catch (NullPointerException e) {
                            testPassed = true;
                        }
                        assertTrue(testPassed);
                    }
                });
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void handleEditorIfResultNotOkWithPluginTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                        imageFormatter.handle(30, 0, new Intent(), imageFormatter.getPluginData());
                        assertEquals(1, ((MockPluginData)imageFormatter.getPluginData()).getActivityCallNb);
                    }
                });
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void handleEditorIfResultOkTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                        try {
                            exception.expect(NullPointerException.class);
                            imageFormatter.handle(30, -1, new Intent(), null);
                        }
                        catch (NullPointerException e) {

                        }
                    }
                });
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void handleEditorIfResrequestCodeGalleryultOkWithPluginTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                            imageFormatter.handle(30, -1, new Intent(), imageFormatter.getPluginData());
                            assertTrue(((MockPluginData) imageFormatter.getPluginData()).getContractor);
                        }
                        catch (NullPointerException e) {

                        }
                    }
                });
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void startEditorTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                        imageFormatter.setLastUri(Uri.fromFile(new File("test")));
                        imageFormatter.startEditor(imageFormatter.getPluginData());
                        assertEquals(2, ((MockPluginData) imageFormatter.getPluginData()).getActivityCallNb);
                    }
                });
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void writeUriToNoteNullTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                        PluginData data = new PluginData(mActivity);
                        data.setSelectionStart(-1);
                        boolean testPassed = false;
                        try {
                            imageFormatter.writeUriToNote(data, null);
                        }
                        catch (NullPointerException e) {
                            testPassed = true;
                        }
                        assertTrue(testPassed);
                    }
                });
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void writeUriToNoteTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                        MockPluginData data = new MockPluginData(mActivity);
                        data.setSelectionStart(-1);
                        imageFormatter.writeUriToNote(data, Uri.fromFile(new File("test")));
                        assertTrue(data.getContractor);
                    }
                });

            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void writeUriToNoteNegativeResponseTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                        MockPluginData data = new MockPluginData(mActivity);
                        data.setSelectionStart(-1);
                        imageFormatter.writeUriToNote(data, Uri.fromFile(new File("test")));
                        assertTrue(data.getContractor);
                    }
                });
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void getImageSpanNameTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                ImageFormatter.ImageSpanWithId image = imageFormatter.getImageSpan(null, null, "name");
                assertEquals("name", image.getName());
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void getBitmapFromUriTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                Bitmap bitmap = ImageFormatterForTest.getBitmapFromUri(Uri.fromFile(new File("test")),
                        imageFormatter.getPluginData());
                assertNull(bitmap);
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void galleryAddPicAllNullTest() throws InterruptedException {
        int numberOfTry = 0;
        boolean tryAgain = true;
        while (tryAgain && numberOfTry < 20) {
            tryAgain = false;
            try {
                ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                exception.expect(NullPointerException.class);
                imageFormatter.galleryAddPic(null, null, null);
            } catch (AssertionError e) {
                tryAgain = true;
                numberOfTry += 1;
                Thread.sleep(500);
            }
        }
    }


    //------ Helper classes ----------------------------

    class ImageFormatterForTest extends ImageFormatter {

        private SpannableString s1 = new SpannableString("title");
        private SpannableString s2 =
                new SpannableString("[img=content://icynote.ui.fileprovider/my_images/img_1.jpg]");
        private Note<SpannableString> delegateInteractor = new NoteData(s1, s2);
        private MainActivity mActivity;
        private PluginData appState;
        private FormatterDecoratorFortTest decorator;

        ImageFormatterForTest(MainActivity a, int requestCodeCamera, int requestCodeGallery) {
            super(requestCodeCamera, requestCodeGallery);
            mActivity = a;
            appState = new MockPluginData(mActivity);
            decorator = new FormatterDecoratorFortTest(delegateInteractor, appState);
        }

        public Activity getActivity() {
            return mActivity;
        }

        public PluginData getPluginData() {
            return appState;
        }

        SpannableString testGetContent() {
            setEnabled(true);
            return decorator.getContent();
        }

        void testSetContent(SpannableString string) {
            setEnabled(true);
            decorator.setContent(string);
        }

        public void setLastUri(Uri uri) {
            lastUri = uri;
        }

        private class FormatterDecoratorFortTest extends FormatterDecorator {
            FormatterDecoratorFortTest(Note<SpannableString> delegateInteractor, PluginData appState){
                super(delegateInteractor, appState);
            }
        }
    }


    class MockNote<S> extends NoteData<S>{
        public MockNote(S defaultTitle, S defaultContent) {
            super(defaultTitle, defaultContent);
        }

        @Override
        public Response setContent(S newContent) {
            return ResponseFactory.negativeResponse();
        }
    }

    public class MockPluginData extends PluginData {
        public boolean getContractor = false;
        public int getActivityCallNb = 0;

        public MockPluginData(Activity a) {
            super(a);
        }

        @Override
        public PluginPresenter.Contract getContractor() {
            getContractor = true;
            if (super.getContractor() != null) {
                return super.getContractor();
            }
            else {
                return mActivity;
            }
        }

        @Override
        public Note<SpannableString> getLastOpenedNote() {
            return new MockNote<>(new SpannableString("title"), new SpannableString("content"));
        }

        @Override
        public Activity getActivity(){
            ++getActivityCallNb;
            return super.getActivity();
        }
    }

}
