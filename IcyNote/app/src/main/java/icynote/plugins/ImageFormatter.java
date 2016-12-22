package icynote.plugins;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import icynote.note.Note;
import icynote.note.Response;
import icynote.note.decorators.NoteDecoratorFactory;
import icynote.note.decorators.NoteDecoratorTemplate;
import icynote.ui.R;
import util.Callback;

/**
 * Image formatter.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
class ImageFormatter implements FormatterPlugin {
    private static final String TAG = ImageFormatter.class.getSimpleName();
    protected static Uri lastUri;

    private final int mRequestCodeCamera;
    private final int mRequestCodeGallery;
    public static final int mRequestCodeEditor = 30;
    private String absolutePath;
    protected boolean isEnabled = false;

    /**
     * Instantiates a new Image formatter.
     *
     * @param requestCodeCamera  the request code camera
     * @param requestCodeGallery the request code gallery
     */
    ImageFormatter(int requestCodeCamera, int requestCodeGallery) {
        mRequestCodeCamera = requestCodeCamera;
        mRequestCodeGallery = requestCodeGallery;
    }

    /* ---------------------------------------------------------------------------------------------
     * METHODS RELATIVE TO THE PLUGIN BEHAVIOR
     * ---------------------------------------------------------------------------------------------
     */

    @Override
    public String getName(){
        return "Image insertion Plugin";
    }

    @Override
    public void setEnabled(boolean enabled) {
        Log.e(TAG, "setEnabled = " + enabled);
        isEnabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public NoteDecoratorFactory<SpannableString> getInteractorFactory(PluginData state) {
        return new FormatterFactory(state);
    }

    @Override
    public Iterable<View> getMetaButtons(final PluginData state) {

        final Activity a = state.getActivity();
        Button takePictureButton = new Button(a.getBaseContext());
        takePictureButton.setBackgroundResource(R.drawable.plugin_photo);
        takePictureButton.setAlpha(0.5f);
        takePictureButton.setContentDescription("make a new picture with the camera");
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeAndInsertNewPhoto(state);
            }
        });


        /*Button usePictureButton = new Button(a.getBaseContext());
        usePictureButton.setBackgroundResource(R.drawable.plugin_gallery);
        usePictureButton.setAlpha(0.5f);
        usePictureButton.setContentDescription("insert a picture from the gallery");
        usePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGallery(state);
            }
        });*/

        ArrayList<View> buttonList = new ArrayList<>();
        buttonList.add(takePictureButton);
        /*buttonList.add(usePictureButton);*/

        return buttonList;
    }

    protected void startGallery(PluginData state) {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        state.getActivity().startActivityForResult(i, mRequestCodeGallery);
    }

    /*private boolean checkPermissionsOnStorage(PluginData state) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(state.getActivity())) {
                state.getActivity().requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
            } else {
                return true;
            }
        }
        return true;

    }*/

    protected class FormatterDecorator extends NoteDecoratorTemplate<SpannableString> {
        private final PluginData appState;

        /**
         * Instantiates a new Formatter decorator.
         *
         * @param delegateInteractor the delegate interactor
         * @param appState           the app state
         */
        FormatterDecorator(Note<SpannableString> delegateInteractor, PluginData appState) {
            super(delegateInteractor);
            this.appState = appState;
        }

        @Override
        public SpannableString getContent() {
            if (!isEnabled) {
                return super.getContent();
            }

            SpannableString contentFromCore = super.getContent();

            Pattern p = Pattern.compile("\\[img=[^\\]]*\\]");
            Matcher m = p.matcher(contentFromCore);
            SpannableString resS = new SpannableString(contentFromCore);
            while (m.find()) {
                int start = m.start();
                int end = m.end();
                String uri = contentFromCore.subSequence(m.start() + 5, m.end() - 1).toString();
                insertSpan(appState, resS, uri, start, end);
            }
            return resS;

        }

        @Override
        public Response setContent(SpannableString newContent) {
            if (!isEnabled){
                return super.setContent(newContent);
            }

            ImageSpanWithId[] spans = newContent.getSpans(
                    0,
                    newContent.length(),
                    ImageSpanWithId.class);

            SpannableString translatedContent = new SpannableString(newContent);
            for (ImageSpanWithId span : spans) {
                Log.i(TAG, "replacing " + span.getName());
                translatedContent.removeSpan(span);
                translatedContent.setSpan("[img=" + span.getName() + "]",
                        newContent.getSpanStart(span),
                        newContent.getSpanEnd(span),
                        newContent.getSpanFlags(span));
            }
            Log.i(TAG, "translated text : " +
                    "\n" + newContent.toString());
            return super.setContent(translatedContent);
        }
    }

    private class FormatterFactory extends NoteDecoratorFactory<SpannableString> {
        private final PluginData appState;

        /**
         * Instantiates a new Formatter factory.
         *
         * @param state the state
         */
        FormatterFactory(PluginData state) {
            appState = state;
        }

        @Override
        public Note<SpannableString> make(Note<SpannableString> delegate) {
            return new FormatterDecorator(delegate, appState);
        }
    }

    /* ---------------------------------------------------------------------------------------------
     * METHODS TO TAKE A PHOTO IN A NEWLY CREATED FILE
     * ---------------------------------------------------------------------------------------------
     */

    protected void takeAndInsertNewPhoto(PluginData state) {
        lastUri = getTempFileUri(state.getActivity());
        startCamera(state);
    }

    protected void startCamera(PluginData state) {
        Activity a = state.getActivity();
        Uri destUri = lastUri;
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (camera.resolveActivity(a.getPackageManager()) == null) {
            Log.e("imageFormatter", "unable to get camera activity");
        } else {
            camera.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            camera.putExtra(MediaStore.EXTRA_OUTPUT, destUri);
            a.startActivityForResult(camera, mRequestCodeCamera);
        }
    }

    protected Uri getTempFileUri(Activity a) {
        File pictureFile = createImageFile(a);
        if (pictureFile == null) {
            throw new AssertionError("unable to create image file");
        }
        absolutePath = pictureFile.getAbsolutePath();
        Uri uri = FileProvider.getUriForFile(a, "icynote.ui.fileprovider", pictureFile);
        if (uri == null) {
            throw new AssertionError("unable to get uri of temp. image file");
        }
        return uri;
    }

    protected File createImageFile(Activity a) {
        try {
            String imageFileName = "img_" + createNameFromStamp();
            File storageDir = a.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
           /* File f = new File(storageDir + File.separator + imageFileName + ".jpg");*/
            File f = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
            absolutePath = f.getAbsolutePath();
            return f;
        } catch (Exception ex) {
            Log.e("imageFormatter", ex.getMessage());
        }
        return null;
    }

    private String createNameFromStamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
    }

    /* ---------------------------------------------------------------------------------------------
     * METHODS TO PROCESS A NEWLY TAKEN PHOTO
     * ---------------------------------------------------------------------------------------------
     */

    @Override
    public boolean canHandle(int requestCode) {
        return mRequestCodeGallery == requestCode || mRequestCodeCamera == requestCode || mRequestCodeEditor == requestCode;
    }

    @Override
    public void handle(int requestCode, int resultCode, Intent data, final PluginData state) {
        Log.i(TAG, "handling " + requestCode);

        if (!canHandle(requestCode)) {
            Log.i(TAG, "aborting: unknown request code " + requestCode);
            throw new AssertionError("unhandled request code");
        }
        if (requestCode == mRequestCodeCamera) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(state.getActivity()
                        , "Sorry, an unexpected error happened.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            //the picture is written in the previously provided uri
            if (lastUri == null) {
                Log.i(TAG, "ignoring requestCode because uri is null");
                Toast.makeText(state.getActivity(),
                        "Unexpected error: file URI is null. Aborted.",
                        Toast.LENGTH_SHORT).show();
            } else {

                startEditor(state);

            }
        } else if (requestCode == mRequestCodeGallery) {
            Toast.makeText(state.getActivity(), "Not implemented yet", Toast.LENGTH_SHORT).show();
        } else if (requestCode == mRequestCodeEditor) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(state.getActivity()
                        , "Sorry, an unexpected error happened.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            else {

                galleryAddPic(state, getBitmapFromUri(lastUri, state), lastUri);
                writeUriToNote(state, lastUri);
                state.getContractor().registerOnStartCallback(new Callback() {
                    @Override
                    public void execute() {
                        state.getContractor().reOpenLastOpenedNote(null);
                    }
                });

            }

        }
        else {
            throw new AssertionError("unhandled request code");
        }

    }

    protected void startEditor(PluginData state) {
        Intent editor = new Intent(state.getActivity(), PictureEditor.class);
        editor.putExtra("uri",lastUri.toString());
        editor.putExtra("absolutePath",absolutePath);
        state.getActivity().startActivityForResult(editor, mRequestCodeEditor);

    }

    protected void writeUriToNote(PluginData state, Uri uri) {

        int selectionStart = state.getSelectionStart();
        int selectionEnd = state.getSelectionEnd();
        if (selectionStart < 0 || selectionEnd < 0) {
            selectionStart = 0;
            selectionEnd = 0;
        }
        int start = Math.min(selectionStart, selectionEnd);
        int end = Math.max(selectionStart, selectionEnd);

        SpannableStringBuilder text = new SpannableStringBuilder(
                state.getLastOpenedNote().getContent());
        text.replace(start, end, "[img=" + uri.toString() + "]");
        Log.i(TAG, "text with uri : " + text);
        Note<SpannableString> note = state.getLastOpenedNote();
        Response r1 = note.setContent(new SpannableString(text));
        state.getContractor().saveNote(note, null);

        if (!r1.isPositive()) {
            Log.i(TAG, "" + r1.isPositive());
        }
    }


    /* ---------------------------------------------------------------------------------------------
     * STATIC METHODS TO GET AND INSERT AN IMAGE
     * ---------------------------------------------------------------------------------------------
     */

    // insert image into ss, at position start to end
    private static void insertSpan(PluginData appState,
                                   SpannableString ss,
                                   String uri,
                                   int start,
                                   int end) {
        Bitmap image = getImage(uri, appState);
        Activity a = appState.getActivity();
        if (a != null) {
            ImageSpanWithId span = getImageSpan(a.getResources(), image, uri);
            ss.setSpan(span, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
    }
    protected static class ImageSpanWithId extends ImageSpan {
        private final String name;

        /**
         * Instantiates a new Image span with id.
         *
         * @param name the name
         * @param d    the d
         * @param flag the flag
         */
        ImageSpanWithId(String name, Drawable d, int flag) {
            super(d, flag);
            this.name = name;
        }

        /**
         * Gets name.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }
    }
    protected static ImageSpanWithId getImageSpan(Resources r, Bitmap image, String name) {
        Drawable d = new BitmapDrawable(r, image);

        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        return new ImageSpanWithId(name, d, ImageSpan.ALIGN_BASELINE);
    }
    private static Bitmap getImage(String name, PluginData appState) {
        Uri uri = Uri.parse(name);

        return getBitmapFromUri(uri, appState);
    }

    protected static Bitmap getBitmapFromUri(Uri uri, PluginData appState) {

        InputStream inputStream = null;
        try {
            Activity activity = appState.getActivity();
            if (activity != null) {
                inputStream = activity.getContentResolver().openInputStream(uri);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap bb = BitmapFactory.decodeStream(inputStream);

        return bb;
    }


    protected void galleryAddPic(PluginData state, Bitmap bitmap, Uri uri) {
        MediaStore.Images.Media.insertImage(
                state.getActivity().getContentResolver(),
                bitmap,
                uri.toString(),
                uri.toString()
        );
    }
}
