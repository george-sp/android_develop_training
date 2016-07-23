package com.codeburrow.displaybitmapsefficiently;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readBitmapDimensionsAndType();
    }


    /**
     * Reads the dimensions and type of the image data
     * prior to construction (and memory allocation) of the bitmap.
     * <p/>
     * The BitmapFactory class provides several decoding methods
     * (decodeByteArray(), decodeFile(), decodeResource(), etc.)
     * for creating a Bitmap from various sources.
     * <p/>
     * These methods attempt to allocate memory for the constructed bitmap
     * and therefore can easily result in an OutOfMemory exception.
     * <p/>
     * Each type of decode method has additional signatures that let you specify
     * decoding options via the BitmapFactory.Options class.
     * <p/>
     * Setting the inJustDecodeBounds property to true while decoding
     * avoids memory allocation, returning null for the bitmap object
     * but setting outWidth, outHeight and outMimeType.
     */
    private void readBitmapDimensionsAndType() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.myimage, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;

        String infoToDisplay = "imageHeight: " + imageHeight + ", imageWidth: " + imageWidth + ", imageType: " + imageType;
        Toast.makeText(MainActivity.this, infoToDisplay, Toast.LENGTH_SHORT).show();
        Log.e(LOG_TAG, infoToDisplay);
    }
}
