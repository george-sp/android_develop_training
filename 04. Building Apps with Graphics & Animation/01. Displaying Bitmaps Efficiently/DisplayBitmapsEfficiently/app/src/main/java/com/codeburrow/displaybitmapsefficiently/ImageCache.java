package com.codeburrow.displaybitmapsefficiently;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.LruCache;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since Aug/02/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
public class ImageCache {

    private static final String LOG_TAG = ImageCache.class.getSimpleName();

    private LruCache<String, BitmapDrawable> mMemoryCache;
    private Set<SoftReference<Bitmap>> mReusableBitmaps;

    // If you're running on Honeycomb or newer, create a
// synchronized HashSet of references to reusable bitmaps.
    if (Utils.hasHoneycomb()) {
        mReusableBitmaps = Collections.synchronizedSet(new HashSet<SoftReference<Bitmap>>());
    }

    mMemoryCache = new LruCache<String, BitmapDrawable>(mCacheParams.memCacheSize) {
        // Notify the removed entry that is no longer being cached.
        @Override
        protected void entryRemoved ( boolean evicted, String key,
            BitmapDrawable oldValue, BitmapDrawable newValue){
        if (RecyclingBitmapDrawable.class.isInstance(oldValue)) {
            // The removed entry is a recycling drawable, so notify it
            // that it has been removed from the memory cache.
            ((RecyclingBitmapDrawable) oldValue).setIsCached(false);
        } else {
            // The removed entry is a standard BitmapDrawable.
            if (Utils.hasHoneycomb()) {
                // We're running on Honeycomb or later, so add the bitmap
                // to a SoftReference set for possible use with inBitmap later.
                mReusableBitmaps.add
                        (new SoftReference<Bitmap>(oldValue.getBitmap()));
            }
        }
    }
    }
}
