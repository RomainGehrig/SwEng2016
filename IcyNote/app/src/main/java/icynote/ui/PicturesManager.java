package icynote.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import icynote.extras.ExtraProvider;

public class PicturesManager extends AppCompatActivity {

    public enum PhotoMethod {
        CAMERA, GALLERY
    }

    static public Map<String,Bitmap> images = new HashMap<>();
    private final int minHeight = 500;
    private final int minWidth = 500;
    static private String currentName = "";
    private ImagePlugin imagePlugin;

    // @TODO check the availibility of the camera with
    // hasSystemFeature(PackageManager.FEATURE_CAMERA)

    private Uri mUri;
    static final int REQUEST_TAKE_PHOTO = 1;
    //static final int REQUEST_IMAGE_CAPTURE = 2;
    static final int RESULT_LOAD_IMAGE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures_manager);

        try {
            checkCameraAvailability();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imagePlugin = new ImagePlugin(getResources());
        int photoMethod = getIntent().getIntExtra("photoMethod",-1);

        if(photoMethod == PhotoMethod.CAMERA.ordinal()) {
            openCamera();
        }
        else if(photoMethod == PhotoMethod.GALLERY.ordinal()) {
            openGallery();
        }
        else {
            //throw new Exception("error on photo method");
        }

        // process text already present in note
        /*editText = (EditText) findViewById(R.id.edittext);
        SpannableString ss = fromTextToCore(new SpannableString(editText.getText()));
        editText.setText(ss);*/
    }


    // onclick
    public void takePicture(View view) {
        openCamera();
    }


    // verify can access to the camera
    private void checkCameraAvailability() throws IOException {
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            throw new IOException();
        }
    }

    public void pickFromGallery(View view) {
        openGallery();
    }


    // onclick button on picture:_manager layout
    public void returnToEditNote(View v) {
        Intent intent = new Intent();
        intent.putExtra("edittextvalue","value_here");
        setResult(RESULT_OK, intent);
        finish();
    }


    public void returnToEditNote(Spannable ss) {
        Intent intent = new Intent();
        intent.putExtra("noteContent",ss.toString());
        intent.putExtra("newImageName", currentName);
        setResult(RESULT_OK, intent);
        finish();
    }


    // open camera activity which will save the photo into a file
    private void openCamera() {

        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (camera.resolveActivity(getPackageManager()) != null) {
            File pictureFile = null;
            try {
                pictureFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("error","ioexception");
            }

            if (pictureFile != null) {
                pictureFile.getName();
                mUri = FileProvider.getUriForFile(this, "icynote.ui.fileprovider", pictureFile);
                camera.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                startActivityForResult(camera, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                Log.i("take photo", "yes");
                // nothing pass by data since we save into a file -> data already in mUri
            }
            if (requestCode == RESULT_LOAD_IMAGE && null != data) {
                Log.i("load image", "yes");
                currentName = createNameFromStamp();
                mUri = data.getData();
            }

            // save image into the storage
            saveImage(mUri);

            // insert id and process view

            String text = getIntent().getStringExtra("noteContentOriginal");
            int start = getIntent().getIntExtra("start",0);
            int end = getIntent().getIntExtra("end",1);

            Log.i("stringOriginal s-e",text +" "+ Integer.toString(start) + " " + Integer.toString(end));

            String newT = insertIdAtSelection(text, start, end);
            SpannableString ss = new SpannableString(newT);
            SpannableString newS = imagePlugin.fromCoreToText(ss);
            returnToEditNote(newS);

        }
    }

    private String createNameFromStamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }


    private void saveImage(Uri uri) {
        // add image in the storage
        Bitmap newB = decodeSampledBitmapFromResource(uri, minWidth, minHeight);
        images.put(currentName,newB);
    }



    /**Sample Size : load less https://developer.android.com/training/displaying-bitmaps/load-bitmap.html **/
    public static int calculateInSampleSize(
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


    public Bitmap decodeSampledBitmapFromResource(Uri uri, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bb = BitmapFactory.decodeStream(inputStream, null, options);


        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        Log.i("w - h", Integer.toString(options.outWidth) +" - "+ Integer.toString(options.outHeight));
        Log.i("newW - newH", Integer.toString(options.outWidth/options.inSampleSize) +" "
                +Integer.toString(options.outHeight/options.inSampleSize));

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // need to create a new inputstream to decode the image again
        Bitmap sizedB = BitmapFactory.decodeStream(is, null, options);

        return sizedB;
    }


    private String insertIdAtSelection(String text, int start, int end) {
        /*Editable text = editText.getText();
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();*/

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(text);
        String imgId = "\n[img=" + currentName + "]\n";
        builder.replace(start, end, imgId);
        return builder.toString();
    }



    // create file depending on date @TODO depending on id ?
    private File createImageFile() throws IOException {
        // Create an image file name
        currentName = createNameFromStamp();
        String imageFileName = "JPEG_" + currentName + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }


    /*******HANDLE CASES WHEN THE IMAGE IS ROTATED
     * OR http://stackoverflow.com/questions/15808719/controlling-the-camera-to-take-pictures-in-portrait-doesnt-rotate-the-final-ima/18915443#18915443
     * *******/
    /*private Bitmap rotatedImage(Bitmap bitmap, Uri uri) {
        Bitmap result = null;
        try {
            ExifInterface exif = new ExifInterface(uri.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            int rotationInDegrees = exifToDegrees(orientation);

            Matrix matrix = new Matrix();
            if (orientation != 0f) {matrix.preRotate(rotationInDegrees);}

            result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    private int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }*/

}
