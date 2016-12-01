package icynote.plugins;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
import icynote.ui.R;
import util.Callback;

import static android.R.attr.minHeight;
import static android.R.attr.minWidth;

class ImageFormatter implements FormatterPlugin {
    private static String TAG = ImageFormatter.class.getSimpleName();
    private static Uri lastUri;

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
    public String getName(){
        return "Image insertion Plugin";
    }

    @Override
    public NoteDecoratorFactory<SpannableString> getInteractorFactory(PluginData state) {
        return new FormatterFactory(state);
    }

    @Override
    public Iterable<View> getMetaButtons(final PluginData state) {

        final Activity a = state.getActivity();
        Button takePictureButton = new Button(a.getBaseContext());
        takePictureButton.setBackgroundResource(R.drawable.meta_photo);
        takePictureButton.setAlpha(0.5f);
        takePictureButton.setContentDescription("make a new picture with the camera");
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeAndInsertNewPhoto(state);
            }
        });


        Button usePictureButton = new Button(a.getBaseContext());
        usePictureButton.setBackgroundResource(R.drawable.meta_gallery);
        usePictureButton.setAlpha(0.5f);
        usePictureButton.setContentDescription("insert a picture from the gallery");
        usePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(a.getBaseContext(), "Gallery not implemented, yet.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        ArrayList<View> buttonList = new ArrayList<>();
        buttonList.add(takePictureButton);
        buttonList.add(usePictureButton);

        return buttonList;
    }

    private static class FormatterDecorator extends NoteDecoratorTemplate<SpannableString> {
        private final PluginData appState;

        FormatterDecorator(Note<SpannableString> delegateInteractor, PluginData appState) {
            super(delegateInteractor);
            this.appState = appState;
        }

        @Override
        public SpannableString getContent() {
            //Log.i(TAG, "getContent");

            SpannableString contentFromCore = super.getContent();

            Pattern p = Pattern.compile("\\[img=[^\\]]*\\]");
            Matcher m = p.matcher(contentFromCore);
            SpannableString resS = new SpannableString(contentFromCore);
            while (m.find()) {
                int start = m.start();
                int end = m.end();
                String uri = contentFromCore.subSequence(m.start() + 5, m.end() - 1).toString();
                //Log.i(TAG, "load image " + uri);
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

    private static class FormatterFactory extends NoteDecoratorFactory<SpannableString> {
        private PluginData appState;

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

    private void takeAndInsertNewPhoto(PluginData state) {
        lastUri = getTempFileUri(state.getActivity());
        assert (lastUri != null);
        startCamera(state);
    }

    private void startCamera(PluginData state) {
        Activity a = state.getActivity();
        Uri destUri = lastUri;
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
        String imageFileName = "img_" + createNameFromStamp();
        File storageDir = a.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File file = new File(storageDir + File.separator + imageFileName + ".jpg"); // add .tmp automatically if null
            return file;
        }
        catch (Exception ex) {
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
        return mRequestCodeGallery == requestCode || mRequestCodeCamera == requestCode;
    }

    @Override
    public void handle(int requestCode, int resultCode, Intent data, final PluginData state) {
        Log.i(TAG, "handling " + requestCode);

        if (!canHandle(requestCode)) {
            Log.i(TAG, "aborting: unknown request code " + requestCode);
            return;
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
                writeUriToNote(state, lastUri);
                state.getContractor().registerOnStartCallback(new Callback() {
                    @Override
                    public void execute() {
                        state.getContractor().reOpenLastOpenedNote(null);
                    }
                });
            }
        } else if (requestCode == mRequestCodeGallery) {
            Toast.makeText(state.getActivity(), "gallery", Toast.LENGTH_SHORT).show();
        } else {
            throw new AssertionError("unhandled request code");
        }

    }

    private void writeUriToNote(PluginData state, Uri uri) {

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
    private static Bitmap getImage(String name, PluginData appState) {
        Uri uri = Uri.parse(name);
        //bitmap = MediaStore.Images.Media.getBitmap(
        //        appState.getActivity().getContentResolver(), uri);
        //return decodeSampledBitmapFromResource(appState, uri, minWidth, minHeight);
        try {
            // TODO rotate if required
            return handleSamplingAndRotationBitmap(appState.getActivity(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static Bitmap decodeSampledBitmapFromResource(PluginData state,
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
    /*private static int calculateInSampleSize(
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
    */

    // ROTATION

    /**
     * This method is responsible for solving the rotation issue if exist. Also scale the images to
     * 1024x1024 resolution
     *
     * @param context       The current context
     * @param selectedImage The Image URI
     * @return Bitmap image results
     * @throws IOException
     */
    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
            throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);


        img = rotateImageIfRequired(context, img, selectedImage);
        return img;
    }


    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from {@link BitmapFactory}. This implementation calculates
     * the closest inSampleSize that will result in the final decoded bitmap having a width and
     * height equal to or larger than the requested width and height. This implementation does not
     * ensure a power of 2 is returned for inSampleSize which can be faster when decoding but
     * results in a larger bitmap which isn't as useful for caching purposes.
     *
     * @param options   An options object with out* params already populated (run through a decode*
     *                  method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }



    /**
     * Rotate an image if required.
     *
     * @param img           The image bitmap
     * @param selectedImage Image URI
     * @return The resulted Bitmap after manipulation
     */
    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        // TODO ne match pas les paths !
        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }



    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

}
