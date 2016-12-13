package icynote.plugins;

import android.graphics.BitmapFactory;

import static org.junit.Assert.*;
import org.junit.Test;

public class ImageFormatterTest {

    private final ImageFormatter formatter = new ImageFormatter(1, 2);

    @Test
    public void getNameTest() {
        assertEquals(formatter.getName(), "Image insertion Plugin");
    }

    @Test
    public void calculateInSampleSizeTest() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.outHeight = 1000;
        options.outWidth = 1500;

        ImageFormatterForTest imageFormatter = new ImageFormatterForTest(0, 0);
        int res = imageFormatter.calculateInSampleSize(options, 100, 100);
        assertEquals(8, res);
        int res2 = imageFormatter.calculateInSampleSize(options, 20, 20);
        assertEquals(32, res2);
    }

}

class ImageFormatterForTest extends ImageFormatter {
        public ImageFormatterForTest(int requestCodeCamera, int requestCodeGallery) {
            super(requestCodeCamera, requestCodeGallery);
        }

        protected static int calculateInSampleSize(BitmapFactory.Options options,
                                                   int reqWidth,
                                                   int reqHeight) {
            return ImageFormatter.calculateInSampleSize(options, reqWidth, reqHeight);
        }
    }
