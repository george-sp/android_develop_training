package com.codeburrow.displaybitmapsefficiently;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

/**
 * A BitmapDrawable that keeps track of whether it is being displayed or cached.
 * When the drawable is no longer being displayed or cached,
 * {@link android.graphics.Bitmap#recycle() recycle()} will be called on this drawable's bitmap.
 * <p>
 * The following code snippet gives an example of calling recycle().
 * It uses reference counting (in the variables mDisplayRefCount and mCacheRefCount)
 * to track whether a bitmap is currently being displayed or in the cache.
 * The code recycles the bitmap when these conditions are met:
 * - The reference count for both mDisplayRefCount and mCacheRefCount is 0.
 * - The bitmap is not null, and it hasn't been recycled yet.
 */
public class RecyclingBitmapDrawable extends BitmapDrawable {

    static final String LOG_TAG = "CountingBitmapDrawable";

    private int mCacheRefCount = 0;
    private int mDisplayRefCount = 0;

    private boolean mHasBeenDisplayed;

    public RecyclingBitmapDrawable(Resources res, Bitmap bitmap) {
        super(res, bitmap);
    }

    /**
     * Notify the drawable that the displayed state has changed.
     * Keep a count to determine when the drawable is no longer displayed.
     *
     * @param isDisplayed - Whether the drawable is being displayed or not
     */
    public void setIsDisplayed(boolean isDisplayed) {
        synchronized (this) {
            if (isDisplayed) {
                mDisplayRefCount++;
                mHasBeenDisplayed = true;
            } else {
                mDisplayRefCount--;
            }
        }
        // Check to see if recycle() can be called.
        checkState();
    }

    /**
     * Notify the drawable that the cache state has changed.
     * Keep a count to determine when the drawable is no longer being cached.
     *
     * @param isCached - Whether the drawable is being cached or not
     */
    public void setIsCached(boolean isCached) {
        synchronized (this) {
            if (isCached) {
                mCacheRefCount++;
            } else {
                mCacheRefCount--;
            }
        }
        // Check to see if recycle() can be called.
        checkState();
    }

    private synchronized void checkState() {
        // If the drawable cache and display ref counts = 0, and this drawable
        // has been displayed, then recycle.
        if (mCacheRefCount <= 0 && mDisplayRefCount <= 0 && mHasBeenDisplayed && hasValidBitmap()) {
            getBitmap().recycle();
        }
    }

    private synchronized boolean hasValidBitmap() {
        Bitmap bitmap = getBitmap();
        return bitmap != null && !bitmap.isRecycled();
    }
}

