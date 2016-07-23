package com.codeburrow.displaybitmapsefficiently;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.image_view);

        /*
         * Load a bitmap of arbitrarily large size
         * into an ImageView that displays a 100x100 pixel thumbnail
         */
        mImageView.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.myimage, 100, 100));
    }

    /**
     * Reads the dimensions and type of the image data
     * prior to construction (and memory allocation) of the bitmap.
     * <p/>
     * To avoid java.lang.OutOfMemory exceptions,
     * check the dimensions of a bitmap before decoding it,
     * unless you absolutely trust the source to provide you
     * with predictably sized image data that comfortably fits within the available memory.
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

    /**
     * Calculates a sample size value that is a power of two based on a target width and height.
     * <p/>
     * Note:
     * A power of two value is calculated because
     * the decoder uses a final value by rounding down to the nearest power of two,
     * as per the inSampleSize documentation.
     * https://developer.android.com/reference/android/graphics/BitmapFactory.Options.html#inSampleSize
     * <p/>
     * To tell the decoder to subsample the image, loading a smaller version into memory,
     * set inSampleSize to true in your BitmapFactory.Options object.
     * <p/>
     * To use this method, first decode with inJustDecodeBounds set to true,
     * pass the options through and then decode again using the new inSampleSize value
     * and inJustDecodeBounds set to false
     *
     * @param options   null-ok; Options that control downsampling and whether the image should be completely decoded, or just is size returned.
     * @param reqWidth  Requested width.
     * @param reqHeight Requested height.
     * @return The largest inSampleSize value that is a power of 2 and keeps both height and width larger than the requested height and width.
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image.
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            /*
             * Calculate the largest inSampleSize value that is a power of 2
             * and keeps both height and width larger than the requested height and width.
             */
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        Log.e(LOG_TAG, "inSampleSize: " + inSampleSize);
        return inSampleSize;
    }

    /**
     * Loads a bitmap of arbitrarily large size.
     *
     * @param res       The resources object containing the image data.
     * @param resId     The resource id of the image data.
     * @param reqWidth  Requested width.
     * @param reqHeight Requested height.
     * @return The decoded Bitmap, or null if the image data could not be decoded.
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions.
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize.
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set.
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}
