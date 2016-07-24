package com.codeburrow.displaybitmapsefficiently;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ImageView mImageView;
    private Bitmap mPlaceHolderBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.image_view);

        /*
         * Start loading the bitmap asynchronously,
         * simply create a new task and execute it.
         */
        loadBitmap(R.drawable.myimage, mImageView);
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

    /**
     * Loads a large image into an ImageView using
     * an AsyncTask and decodeSampleBitmapFromResource().
     * <p/>
     * Before executing the BitmapWorkerTask (AsyncTask),
     * you create an AsyncDrawable and bind it to the target ImageView.
     * <p/>
     * This implementation is now suitable for use in ListView and GridView components
     * as well as any other components that recycle their child views.
     * Simply call loadBitmap where you normally set an image to your ImageView.
     * <p/>
     * For example, in a GridView implementation this would be
     * in the getView() method of the backing adapter.
     *
     * @param resId
     * @param imageView
     */
    public void loadBitmap(int resId, ImageView imageView) {
        if (cancelPotentialWork(resId, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            final AsyncDrawable asyncDrawable = new AsyncDrawable(getResources(), mPlaceHolderBitmap, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(resId);
        }
    }


    /**
     * Checks if another running task is already associated with the ImageView.
     * If so, it attempts to cancel the previous task by calling cancel().
     * In a small number of cases, the new task data matches the existing task
     * and nothing further needs to happen.
     *
     * @param data
     * @param imageView
     * @return
     */
    public static boolean cancelPotentialWork(int data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final int bitmapData = bitmapWorkerTask.data;
            // If bitmapData is not yet set or it differs from the new data.
            if (bitmapData == 0 || bitmapData != data) {
                // Cancel previous task.
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress.
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled.
        return true;
    }

    /**
     * Helper Method.
     * <p/>
     * Retrieves the task associated with a particular ImageView.
     *
     * @return
     */
    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    /**
     * The AsyncTask class provides an easy way to execute some work in a background thread
     * and publish the results back on the UI thread.
     */
    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {

        /*
         * The WeakReference to the ImageView ensures that
         * the AsyncTask does not prevent the ImageView and anything it references
         * from being garbage collected.
         *
         * There’s no guarantee the ImageView is still around when the task finishes,
         * so you must also check the reference in onPostExecute().
         *
         * The ImageView may no longer exist, if for example,
         * the user navigates away from the activity or
         * if a configuration change happens before the task finishes.
         */
        private final WeakReference<ImageView> imageViewReference;
        private int data = 0;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {
            data = params[0];
            return decodeSampledBitmapFromResource(getResources(), data, 100, 100);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // Check if the task is cancelled.
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
                // Check if the current task matches the one associated with the ImageView.
                if (this == bitmapWorkerTask && imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    /**
     * Create a dedicated Drawable subclass to store a reference back to the worker task.
     * In this case, a BitmapDrawable:
     * https://developer.android.com/reference/android/graphics/drawable/BitmapDrawable.html
     * is used so that a placeholder image can be displayed in the ImageView
     * while the task completes.
     */
    static class AsyncDrawable extends BitmapDrawable {

        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference = new WeakReference<>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }
}
