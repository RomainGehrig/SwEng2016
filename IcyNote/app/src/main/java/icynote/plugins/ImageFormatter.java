package icynote.plugins;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
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
import java.io.IOException;
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
import icynote.ui.utils.ApplicationState;

import static android.R.attr.minHeight;
import static android.R.attr.minWidth;

class ImageFormatter implements FormatterPlugin {

    private final int mRequestCodeCamera;
    private final int mRequestCodeGallery;

    ImageFormatter(int requestCodeCamera, int requestCodeGallery) {
        mRequestCodeCamera = requestCodeCamera;
        mRequestCodeGallery = requestCodeGallery;
    }

    /* ---------------------------------------------------------------------------------------------
     * METHODS RELATIVE TO THE PLUGIN BEHAVIOR
     * ---------------------------------------------------------------------------------------------
     */

    @Override
    public NoteDecoratorFactory<SpannableString> getInteractorFactory(ApplicationState state) {
        return new FormatterFactory(state);
    }

    @Override
    public Iterable<View> getMetaButtons(final ApplicationState state) {

        final AppCompatActivity a = state.getActivity();
        Button takePictureButton = new Button(a.getBaseContext());
        takePictureButton.setText("insert photo from camera");
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(a, "starting camera", Toast.LENGTH_SHORT).show();
                takeAndInsertNewPhoto(state);
            }
        });


        Button usePictureButton = new Button(a.getBaseContext());
        usePictureButton.setText("insert picture from gallery");
        usePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(a.getBaseContext(), "starting gallery", Toast.LENGTH_SHORT).show();
                //todo
            }
        });

        ArrayList<View> buttonList = new ArrayList<>();
        buttonList.add(takePictureButton);
        buttonList.add(usePictureButton);

        return buttonList;
    }

    private static class FormatterDecorator extends NoteDecoratorTemplate<SpannableString> {
        private final ApplicationState appState;

        FormatterDecorator(Note<SpannableString> delegateInteractor, ApplicationState appState) {
            super(delegateInteractor);
            this.appState = appState;
        }

        @Override
        public SpannableString getContent() {
            //Log.i("ImgFormatter.Decorator", "getContent");

            SpannableString contentFromCore = super.getContent();

            Pattern p = Pattern.compile("\\[img=[^\\]]*\\]");
            Matcher m = p.matcher(contentFromCore);
            SpannableString resS = new SpannableString(contentFromCore);
            while (m.find()) {
                int start = m.start();
                int end = m.end();
                String uri = contentFromCore.subSequence(m.start() + 5, m.end() - 1).toString();
                Log.i("ImageFormatter", "load image " + uri);
                insertSpan(appState, resS, uri, start, end);
            }
            return resS;

        }

        @Override
        public Response setContent(SpannableString newContent) {
            ImageSpanWithId[] spans = newContent.getSpans(
                    0,
                    newContent.length(),
                    ImageSpanWithId.class);

            SpannableString translatedContent = new SpannableString(newContent);
            for (ImageSpanWithId span : spans) {
                Log.i("ImgFormatter.setContent", "replacing " + span.getName());
                translatedContent.removeSpan(span);
                translatedContent.setSpan("[img=" + span.getName() + "]",
                        newContent.getSpanStart(span),
                        newContent.getSpanEnd(span),
                        newContent.getSpanFlags(span));
            }
            Log.i("ImgFormatter.setContent", "translated text : " +
                    "\n" + newContent.toString());
            return super.setContent(translatedContent);
        }
    }

    private static class FormatterFactory extends NoteDecoratorFactory<SpannableString> {
        private ApplicationState appState;

        FormatterFactory(ApplicationState state) {
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

    private void takeAndInsertNewPhoto(ApplicationState state) {
        state.setTempFileUri(getTempFileUri(state.getActivity()));
        assert (state.getTempFileUri() != null);
        startCamera(state);
    }

    private void startCamera(ApplicationState state) {
        Activity a = state.getActivity();
        Uri destUri = state.getTempFileUri();
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (camera.resolveActivity(a.getPackageManager()) == null) {
            Log.e("imageFormatter", "unable to get camera activity");
        } else {
            camera.putExtra(MediaStore.EXTRA_OUTPUT, destUri);
            a.startActivityForResult(camera, mRequestCodeCamera);
        }
    }
    private Uri getTempFileUri(Activity a) {
        File pictureFile = createImageFile(a);
        if (pictureFile == null) {
            throw new AssertionError("unable to create image file");
        }
        Uri uri = FileProvider.getUriForFile(a, "icynote.ui.fileprovider", pictureFile);
        if (uri == null) {
            throw new AssertionError("unable to get uri of temp. image file");
        }
        return uri;
    }

    private File createImageFile(Activity a) {
        try {
            String imageFileName = "img_" + createNameFromStamp();
            File storageDir = a.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            return File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
        } catch (IOException ex) {
            Log.e("imageFormatter", ex.getMessage());
        }
        return null;
    }

    private String createNameFromStamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
    }

    /* ---------------------------------------------------------------------------------------------
     * METHODS TO TAKE PROCESS A NEWLY TAKEN PHOTO
     * ---------------------------------------------------------------------------------------------
     */

    @Override
    public boolean canHandle(int requestCode) {
        return mRequestCodeGallery == requestCode || mRequestCodeCamera == requestCode;
    }

    @Override
    public void handle(int requestCode, int resultCode, Intent data, ApplicationState state) {
        Log.i("imageFormatter", "handling " + requestCode);

        if (!canHandle(requestCode)) {
            Log.i("imageFormatter", "aborting: unknown request code " + requestCode);
            return;
        }
        if (requestCode == mRequestCodeCamera) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(state.getActivity()
                        , "an error happened", Toast.LENGTH_SHORT).show();
                return;
            }
            //the picture is written in the previously provided uri
            if (state.getTempFileUri() == null) {
                Log.i("imageFormatter", "ignoring requestCode because uri is null");
                Toast.makeText(state.getActivity(),
                        "file URI is null. Aborted.",
                        Toast.LENGTH_SHORT).show();
            } else {
                writeUriToNote(state, state.getTempFileUri());
                state.getActivity().onBackPressed();
                //insertImageInNoteContent(state, state.getTempFileUri());
            }
        } else if (requestCode == mRequestCodeGallery) {
            Toast.makeText(state.getActivity(), "gallery", Toast.LENGTH_SHORT).show();
        } else {
            throw new AssertionError("unhandled request code");
        }

    }

    private void writeUriToNote(ApplicationState state, Uri uri) {

        int selectionStart = state.getSelectionStart();
        int selectionEnd = state.getSelectionEnd();
        if (selectionStart < 0 || selectionEnd < 0) {
            selectionStart = 0;
            selectionEnd = 0;
        }
        int start = Math.min(selectionStart, selectionEnd);
        int end = Math.max(selectionStart, selectionEnd);

        SpannableStringBuilder text = new SpannableStringBuilder(state.getLastOpenedNoteContent());
        text.replace(start, end, "[img=" + uri.toString() + "]");
        Log.i("ImgFormatter", "text with uri : " + text);
        Note<SpannableString> note = state.getLastOpenedNote();
        Response r1 = note.setContent(new SpannableString(text));
        Response r2 = state.getNoteProvider().persist(note);

        if (!r1.isPositive() || !r2.isPositive()) {
            Log.i("BIG MISTAKE", r1.isPositive() + " / " + r2.isPositive());
        }
    }


    /* ---------------------------------------------------------------------------------------------
     * STATIC METHODS TO GET AND INSERT AN IMAGE
     * ---------------------------------------------------------------------------------------------
     */

    // insert image into ss, at position start to end
    private static void insertSpan(ApplicationState appState,
                                   SpannableString ss,
                                   String uri,
                                   int start,
                                   int end) {
        Bitmap image = getImage(uri, appState);
        Activity a = appState.getActivity();
        ImageSpanWithId span = getImageSpan(a.getResources(), image, uri);
        ss.setSpan(span, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
    }
    private static class ImageSpanWithId extends ImageSpan {
        private String name;

        ImageSpanWithId(String name, Drawable d, int flag) {
            super(d, flag);
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
    private static ImageSpanWithId getImageSpan(Resources r, Bitmap image, String name) {
        Drawable d = new BitmapDrawable(r, image);
        // TODO image peut dépasser un peu sur le côté
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        return new ImageSpanWithId(name, d, ImageSpan.ALIGN_BASELINE);
    }
    private static Bitmap getImage(String name, ApplicationState appState) {
        Uri uri = Uri.parse(name);
        //bitmap = MediaStore.Images.Media.getBitmap(
        //        appState.getActivity().getContentResolver(), uri);
        return decodeSampledBitmapFromResource(appState, uri, minWidth, minHeight);
    }
    private static Bitmap decodeSampledBitmapFromResource(ApplicationState state,
                                                          Uri uri, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        InputStream inputStream = null;
        try {
            inputStream = state.getActivity().getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bb = BitmapFactory.decodeStream(inputStream, null, options);


        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream is = null;
        try {
            is = state.getActivity().getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // need to create a new inputstream to decode the image again

        return BitmapFactory.decodeStream(is, null, options);
    }
    /**
     * Sample Size : load less https://developer.android.com/training/displaying-bitmaps/load-bitmap.html
     **/
    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
