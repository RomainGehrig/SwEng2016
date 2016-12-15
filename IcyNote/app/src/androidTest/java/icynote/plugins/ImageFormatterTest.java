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
        mActivity = main.getActivity();
    }

    @Test
    public void getNameTest() {
        assertEquals(formatter.getName(), "Image insertion Plugin");
    }

    @Test
    public void getContentIfNotEnabledTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 0, 0);
        SpannableString res = imageFormatter.testGetContent();
        assertEquals("[img=content://icynote.ui.fileprovider/my_images/img_1.jpg]", res.toString());
    }

    @Test
    public void setContentIfNotEnabledTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 0, 0);
        imageFormatter.testSetContent(new SpannableString("new body"));
        SpannableString res = imageFormatter.testGetContent();
        assertEquals("new body", res.toString());
    }
    @Test
    public void createImageFileWithNullTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 0, 0);
        File res = imageFormatter.createImageFile(null);
        assertNull(res);
    }

    @Test
    public void createImageFileTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 0, 0);
        File res = imageFormatter.createImageFile(imageFormatter.getActivity());
        assertNotNull(res);
    }

    @Test
    public void getTempFileUriWithNullTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 0, 0);
        exception.expect(AssertionError.class);
        imageFormatter.getTempFileUri(null);
    }

    @Test
    public void getTempFileUriTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 0, 0);
        Uri res = imageFormatter.getTempFileUri(imageFormatter.getActivity());
        assertEquals("/my_images/img_",res.getPath().substring(0, 15));
    }

    @Test
    public void startCameraWithNullTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 0, 0);
        exception.expect(NullPointerException.class);
        imageFormatter.startCamera(null);
    }

    @Test
    public void takeAndInsertNewPhotoWithNullTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 0, 0);
        exception.expect(NullPointerException.class);
        imageFormatter.takeAndInsertNewPhoto(null);
    }

    @Test
    public void startGalleryWithNullTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 0, 0);
        exception.expect(NullPointerException.class);
        imageFormatter.startGallery(null);
    }



    @Test
    public void canHandleTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
        assertTrue(imageFormatter.canHandle(10));
        assertTrue(imageFormatter.canHandle(20));
        assertTrue(imageFormatter.canHandle(30));
        assertFalse(imageFormatter.canHandle(40));
        assertFalse(imageFormatter.canHandle(0));
        assertFalse(imageFormatter.canHandle(15));
    }

    @Test
    public void handleAnUnknownRequestCodeTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
        exception.expect(AssertionError.class);
        imageFormatter.handle(15, 0, null, null);
    }

    @Test
    public void handleACameraIfResultNotOkNullTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
        exception.expect(NullPointerException.class);
        imageFormatter.handle(10, 0, null, null);
    }

    @Test
    public void handleACameraIfResultNotOkTest() {
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                imageFormatter.handle(10, 0, null, imageFormatter.getPluginData());
                assertEquals(1, ((MockPluginData)imageFormatter.getPluginData()).getActivityCallNb);
            }
        });
    }

    @Test
    public void handleACameraIfUriNullNoPluginTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
        imageFormatter.setLastUri(null);
        exception.expect(NullPointerException.class);
        imageFormatter.handle(10, -1, null, null);
    }

    @Test
    public void handleACameraIfUriNullWithPluginTest() {
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                imageFormatter.setLastUri(null);
                imageFormatter.handle(10, -1, null, imageFormatter.getPluginData());
                assertEquals(1, ((MockPluginData)imageFormatter.getPluginData()).getActivityCallNb);
            }
        });
    }

    @Test
    public void handleACameraIfUriNullAndResultNotOkTest() {
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                imageFormatter.setLastUri(null);
                imageFormatter.handle(10, 0, null, imageFormatter.getPluginData());
                assertEquals(1, ((MockPluginData)imageFormatter.getPluginData()).getActivityCallNb);
            }
        });
    }

    @Test
    public void handleACameraIfUriAndResultOkNullTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
        imageFormatter.setLastUri(Uri.fromFile(new File("test")));
        exception.expect(NullPointerException.class);
        imageFormatter.handle(10, -1, null, null);
    }

    @Test
    public void handleACameraIfUriAndResultOkTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
        imageFormatter.setLastUri(Uri.fromFile(new File("test")));
        imageFormatter.handle(10, -1, null, imageFormatter.getPluginData());
        assertEquals(2, ((MockPluginData)imageFormatter.getPluginData()).getActivityCallNb);
    }

    @Test
    public void handleGalleryIfResultNotOkTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
        exception.expect(NullPointerException.class);
        imageFormatter.handle(20, 0, null, null);
    }

    @Test
    public void handleGalleryIfResultNotOkWithPluginTest() {
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                imageFormatter.handle(20, 0, null, imageFormatter.getPluginData());
                assertEquals(1, ((MockPluginData)imageFormatter.getPluginData()).getActivityCallNb);
            }
        });
    }

    @Test
    public void handleGalleryIfResultOkTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
        exception.expect(NullPointerException.class);
        imageFormatter.handle(20, -1, new Intent(), null);
    }

    @Test
    public void handleGalleryIfResultOkWithPluginTest() {
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
    }

    @Test
    public void handleEditorIfResultNotOkTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
        exception.expect(NullPointerException.class);
        imageFormatter.handle(30, 0, new Intent(), null);
    }

    @Test
    public void handleEditorIfResultNotOkWithPluginTest() {
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                imageFormatter.handle(30, 0, new Intent(), imageFormatter.getPluginData());
                assertEquals(1, ((MockPluginData)imageFormatter.getPluginData()).getActivityCallNb);
            }
        });
    }

    @Test
    public void handleEditorIfResultOkTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
        exception.expect(NullPointerException.class);
        imageFormatter.handle(30, -1, new Intent(), null);
    }

    @Test
    public void handleEditorIfResrequestCodeGalleryultOkWithPluginTest() {
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
    }

    @Test
    public void handleWithUnknownRequestCodeTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
        exception.expect(AssertionError.class);
        imageFormatter.handle(15, -1, new Intent(), imageFormatter.getPluginData());
    }

    @Test
    public void startEditorTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
        imageFormatter.setLastUri(Uri.fromFile(new File("test")));
        imageFormatter.startEditor(imageFormatter.getPluginData());
        assertEquals(2, ((MockPluginData)imageFormatter.getPluginData()).getActivityCallNb);
    }

    @Test
    public void writeUriToNoteNullTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
        PluginData data = new PluginData(mActivity);
        data.setSelectionStart(-1);
        exception.expect(NullPointerException.class);
        imageFormatter.writeUriToNote(data, null);
    }

    @Test
    public void writeUriToNoteTest() {
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                MockPluginData data = new MockPluginData(mActivity);
                data.setSelectionStart(-1);
                imageFormatter.writeUriToNote(data, Uri.fromFile(new File("test")));
                assertTrue(data.getContractor);
            }
        });
    }

    @Test
    public void writeUriToNoteNegativeResponseTest() {
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
                MockPluginData data = new MockPluginData(mActivity);
                data.setSelectionStart(-1);
                imageFormatter.writeUriToNote(data, Uri.fromFile(new File("test")));
                assertTrue(data.getContractor);
            }
        });
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
            /*if (super.getLastOpenedNote() != null) {
                return super.getLastOpenedNote();
            }
            else {
                return new NoteData(new SpannableString("title"), new SpannableString("content"));
            }*/
        }

        @Override
        public Activity getActivity(){
            ++getActivityCallNb;
            return super.getActivity();
        }
    }

    @Test
    public void getImageSpanNameTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
        ImageFormatter.ImageSpanWithId image = imageFormatter.getImageSpan(null, null, "name");
        assertEquals("name", image.getName());
    }

    @Test
    public void getBitmapFromUriTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
        Bitmap bitmap = ImageFormatterForTest.getBitmapFromUri(Uri.fromFile(new File("test")),
                imageFormatter.getPluginData());
        assertNull(bitmap);
    }

    @Test
    public void galleryAddPicAllNullTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
        exception.expect(NullPointerException.class);
        imageFormatter.galleryAddPic(null, null);
    }

    @Test
    public void galleryAddPicNullBitmapTest() {
        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(mActivity, 10, 20);
        imageFormatter.galleryAddPic(imageFormatter.getPluginData(), null);
    }

    class ImageFormatterForTest extends ImageFormatter {

        private SpannableString s1 = new SpannableString("title");
        private SpannableString s2 =
                new SpannableString("[img=content://icynote.ui.fileprovider/my_images/img_1.jpg]");
        private Note<SpannableString> delegateInteractor = new NoteData(s1, s2);
        private MainActivity mActivity;
        private PluginData appState;
        private FormatterDecoratorFortTest decorator;

        public ActivityTestRule<MainActivity> main;

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

}
