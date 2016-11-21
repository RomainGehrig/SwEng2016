package icynote.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kl on 20.11.2016.
 */

public class ImagePlugin {

    private Map<String, Bitmap> images;
    private Resources resources;

    public ImagePlugin(Resources res) {
        images = PicturesManager.images;
        resources = res;
    }


    public SpannableString fromCoreToText(SpannableString ss) {

        String text = ss.toString();
        Pattern p = Pattern.compile("\\[img=[AZ-a-z0-9]*\\]");
        Matcher m = p.matcher(ss);
        SpannableString resS = new SpannableString(ss);
        while (m.find()){
            int start = m.start();
            int end = m.end();
            String name = text.substring(m.start()+5, m.end()-1);
            try {
                Log.i("load image name", name);
                Log.i("start", Integer.toString(start));
                insertSpan(resS, name, images.get(name), start, end);
            }
            catch (IndexOutOfBoundsException e) {
                throw new IndexOutOfBoundsException("Image name " + name + " does not exist");
            }
        }
        return resS;
    }


    private SpannableString fromTextToCore(SpannableString ss) {

        ImageSpanWithId[] spans = ss.getSpans(0, ss.toString().length(),ImageSpanWithId.class);
        SpannableString newS = new SpannableString(ss);
        for(int i = 0; i < spans.length; i++) {
            newS.removeSpan(spans[i]);
            newS.setSpan("[img="+spans[i].getName()+"]", ss.getSpanStart(spans[i]), ss.getSpanEnd(spans[i]),
                    ss.getSpanFlags(spans[i]));
        }
        return newS;
    }


    // insert image into ss, at position start to end
    public void insertSpan(SpannableString ss, String name, Bitmap image, int start, int end) {
        Drawable d = new BitmapDrawable(resources, image);
        // TODO image peut dépasser un peu sur le côté
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpanWithId span = new ImageSpanWithId(name, d, ImageSpan.ALIGN_BASELINE);
        ss.setSpan(span, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE); // change index
    }


}
