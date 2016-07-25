package com.codeburrow.displaybitmapsefficiently;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    // LruCache: https://developer.android.com/reference/android/util/LruCache.html
    private LruCache<String, Bitmap> mMemoryCache;
    private ImageView mImageView;
    private Bitmap mPlaceHolderBitmap;
    private DiskLruCache mDiskLruCache;
    private final Object mDiskCacheLock = new Object();
    private boolean mDiskCacheStarting = true;
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
    private static final String DISK_CACHE_SUBDIR = "thumbnails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * Note:
         * In this example, one eighth of the application memory is allocated for our cache.
         * On a normal/hdpi device this is a minimum of around 4MB (32/8).
         * A full screen GridView filled with images on a device with 800x480 resolution
         * would use around 1.5MB (800*480*4 bytes),
         * so this would cache a minimum of around 2.5 pages of images in memory.
         */
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        // Initialize memory cache.
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
        // Initialize disk cache on background thread.
        File cacheDir = getDiskCacheDir(this, DISK_CACHE_SUBDIR);
        new InitDiskCacheTask().execute(cacheDir);

        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.image_view);

        /*
         * Start loading the bitmap asynchronously,
         * simply create a new task and execute it.
         */
        loadBitmap(R.drawable.myimage, mImageView);
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
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
     * When loading a bitmap into an ImageView, the LurCache is checked first.
     * If an entry is found, it is used immediately to update the ImageView,
     * otherwise a background thread is spawned to process the image.
     *
     * @param resId
     * @param imageView
     */
    public void loadBitmap(int resId, ImageView imageView) {
        final String imageKey = String.valueOf(resId);

        final Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if (bitmap != null) {
            mImageView.setImageBitmap(bitmap);
        } else {
            mImageView.setImageResource(R.drawable.image_placeholder);
            BitmapWorkerTask task = new BitmapWorkerTask(mImageView);
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

    class InitDiskCacheTask extends AsyncTask<File, Void, Void> {

        @Override
        protected Void doInBackground(File... params) {
            synchronized (mDiskCacheLock) {
                File cacheDir = params[0];
                mDiskLruCache = DiskLruCache.open(cacheDir, DISK_CACHE_SIZE);
                mDiskCacheStarting = false; // Finished initialization
                mDiskCacheLock.notifyAll(); // Wake any waiting threads
            }
            return null;
        }
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
            final String imageKey = String.valueOf(params[0]);

            // Check disk cache in background thread
            Bitmap bitmap = getBitmapFromDiskCache(imageKey);

            if (bitmap == null) { // Not found in disk cache
                // Process as normal
                final Bitmap bitmap = decodeSampledBitmapFromResource(
                        getResources(), params[0], 100, 100));
            }

            // Add final bitmap to caches
            addBitmapToCache(imageKey, bitmap);

            return bitmap;
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


    public void addBitmapToCache(String key, Bitmap bitmap) {
        // Add to memory cache as before
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }

        // Also add to disk cache
        synchronized (mDiskCacheLock) {
            if (mDiskLruCache != null && mDiskLruCache.get(key) == null) {
                mDiskLruCache.put(key, bitmap);
            }
        }
    }

    public Bitmap getBitmapFromDiskCache(String key) {
        synchronized (mDiskCacheLock) {
            // Wait while disk cache is started from background thread
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLock.wait();
                } catch (InterruptedException e) {
                }
            }
            if (mDiskLruCache != null) {
                return mDiskLruCache.get(key);
            }
        }
        return null;
    }

    /*
     * Creates a unique subdirectory of the designated app cache directory.
     * Tries to use external but if not mounted, falls back on internal storage.
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() :
                        context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
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
