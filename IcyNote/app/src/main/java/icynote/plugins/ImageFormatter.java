package icynote.plugins;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import icynote.note.Note;
import icynote.note.Response;
import icynote.note.decorators.NoteDecoratorFactory;
import icynote.note.decorators.NoteDecoratorTemplate;
import icynote.ui.MainActivity;
import icynote.ui.R;

public class ImageFormatter implements FormatterPlugin {
    private static Map<String, Bitmap> images = new HashMap<>();
    private final int mRequestCodeCamera;
    private final int mRequestCodeGallery;


    public ImageFormatter(int requestCodeCamera, int requestCodeGallery) {
        mRequestCodeCamera = requestCodeCamera;
        mRequestCodeGallery = requestCodeGallery;
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public NoteDecoratorFactory<SpannableString> getInteractorFactory() {
        return new FormatterFactory();
    }

    @Override
    public Iterable<View> getMetaButtons(final Activity a) {

        Button takePictureButton = new Button(a.getBaseContext());
        takePictureButton.setText("insert photo from camera");
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(a.getBaseContext(), "starting camera", Toast.LENGTH_SHORT).show();
                takeAndInsertNewPhoto(a);
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

    private void takeAndInsertNewPhoto(Activity a) {
        Uri destFile = getTempFileUri(a);
        writeUriToNote(a, destFile);
        startCamera(a, destFile);
    }

    private Uri getTempFileUri(Activity a) {
        File pictureFile = createImageFile(a);
        if (pictureFile == null) {
            return null;
        } else {
            return FileProvider.getUriForFile(a, "icynote.ui.fileprovider", pictureFile);
        }
    }

    private File createImageFile(Activity a) {
        try {
            String imageFileName = "img_" + createNameFromStamp();
            File storageDir = a.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
            return image;
        } catch (IOException ex) {
            Log.e("imageFormatter", ex.getMessage());
        }
        return null;
    }

    private String createNameFromStamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
    }

    private void writeUriToNote(Activity a, Uri uri) {
        View view = MainActivity.editNoteView;
        EditText noteContent = (EditText) view.findViewById(R.id.noteDisplayBodyText);
        if (noteContent == null) {
            Toast.makeText(a.getBaseContext(), "unable to change note content",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String uriTag = " [img " + uri.toString() + "] ";

        int selectionStart = noteContent.getSelectionStart();
        int selectionEnd = noteContent.getSelectionEnd();

        if (selectionStart < 0 || selectionEnd < 0) {
            noteContent.append(uriTag);
        } else {
            int start = Math.min(selectionStart, selectionEnd);
            int end = Math.max(selectionStart, selectionEnd);
            noteContent.getText().replace(start, end, uriTag, 0, uriTag.length());
        }
    }

    private void startCamera(Activity a, Uri destUri) {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (camera.resolveActivity(a.getPackageManager()) == null) {
            Log.e("imageFormatter", "unable to get camera activity");
        } else {
            camera.putExtra(MediaStore.EXTRA_OUTPUT, destUri);
            a.startActivityForResult(camera, mRequestCodeCamera);
        }
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public boolean canHandle(int requestCode) {
        return mRequestCodeGallery == requestCode || mRequestCodeCamera == requestCode;
    }

    @Override
    public void handle(int requestCode, int resultCode, Intent data, Activity activity) {
        Log.i("imageFormatter", "handling " + requestCode);
        if (!canHandle(requestCode)) {
            Log.i("imageFormatter", "aborting: unknown request code " + requestCode);
            return;
        }
        if (requestCode == mRequestCodeCamera) {
            //the picture is written in the previously provided uri
            Toast.makeText(activity.getBaseContext(), "photo available", Toast.LENGTH_SHORT).show();
        } else if (requestCode == mRequestCodeGallery) {
            Toast.makeText(activity.getBaseContext(), "photo available", Toast.LENGTH_SHORT).show();
        } else {
            throw new AssertionError("unhandled request code");
        }

    }

    //----------------------------------------------------------------------------------------------

    private static class FormatterDecorator extends NoteDecoratorTemplate<SpannableString> {
        FormatterDecorator(Note<SpannableString> delegateInteractor) {
            super(delegateInteractor);
        }

        @Override
        public SpannableString getContent() {
            //todo
            return super.getContent();
        }

        @Override
        public Response setContent(SpannableString newContent) {
            //todo
            return super.setContent(newContent);
        }
    }

    private static class FormatterFactory extends NoteDecoratorFactory<SpannableString> {
        @Override
        public Note<SpannableString> make(Note<SpannableString> delegate) {
            return new FormatterDecorator(delegate);
        }
    }

    public SpannableString fromCoreToText(Activity a, SpannableString ss) {

        String text = ss.toString();
        Pattern p = Pattern.compile("\\[img=[AZ-a-z0-9]*\\]");
        Matcher m = p.matcher(ss);
        SpannableString resS = new SpannableString(ss);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            String name = text.substring(m.start() + 5, m.end() - 1);
            try {
                Log.i("load image name", name);
                Log.i("start", Integer.toString(start));
                insertSpan(a, resS, name, images.get(name), start, end);
            } catch (IndexOutOfBoundsException e) {
                throw new IndexOutOfBoundsException("Image name " + name + " does not exist");
            }
        }
        return resS;
    }

    public SpannableString fromTextToCore(Spannable ss) {

        ImageSpanWithId[] spans = ss.getSpans(0, ss.toString().length(), ImageSpanWithId.class);
        SpannableString newS = new SpannableString(ss);
        for (int i = 0; i < spans.length; i++) {
            newS.removeSpan(spans[i]);
            newS.setSpan("[img=" + spans[i].getName() + "]", ss.getSpanStart(spans[i]), ss.getSpanEnd(spans[i]),
                    ss.getSpanFlags(spans[i]));
        }
        return newS;
    }

    // insert image into ss, at position start to end
    private void insertSpan(Activity a, SpannableString ss, String name, Bitmap image, int start, int end) {
        Drawable d = new BitmapDrawable(a.getResources(), image);
        // TODO image peut dépasser un peu sur le côté
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpanWithId span = new ImageSpanWithId(name, d, ImageSpan.ALIGN_BASELINE);
        ss.setSpan(span, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE); // change index
    }

    public class ImageSpanWithId extends ImageSpan {

        private String name;

        public ImageSpanWithId(String name, Drawable d, int flag) {
            super(d, flag);
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

/*
    public void takePhotoFromGallery(Activity a) {
        Intent intent = new Intent(a, PicturesManager.class);
        View view = MainActivity.editNoteView;
        EditText et = (EditText) view.findViewById(R.id.noteDisplayBodyText);
        intent.putExtra("noteContentOriginal", "");
        intent.putExtra("start", et.getSelectionStart());
        intent.putExtra("end", et.getSelectionEnd());
        a.startActivityForResult(intent, PicturesManager.REQUEST_GALLERY);
    }
*/

}
