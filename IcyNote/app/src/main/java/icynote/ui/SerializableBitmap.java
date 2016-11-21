package icynote.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class SerializableBitmap implements Serializable {

    private Bitmap mBitmap;

    SerializableBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.PNG, 0, byteStream);
        byte[] bitmapBytes = byteStream.toByteArray();
        out.write(bitmapBytes, 0, bitmapBytes.length);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int b = 0;
        b = in.read();
        while(b != -1) {
            byteStream.write(b);
            b = in.read();
        }
        byte[] bitmapBytes = byteStream.toByteArray();
        mBitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
    }
}
