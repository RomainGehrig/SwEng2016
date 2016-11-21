package icynote.ui;

import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

/**
 * Created by kl on 18.11.2016.
 */

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
