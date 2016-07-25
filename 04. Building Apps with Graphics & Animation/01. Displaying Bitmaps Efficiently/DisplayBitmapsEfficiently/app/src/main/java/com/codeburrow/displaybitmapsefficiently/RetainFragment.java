package com.codeburrow.displaybitmapsefficiently;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.LruCache;

/**
 * Runtime configuration changes, such as a screen orientation change,
 * cause Android to destroy and restart the running activity with the new configuration
 * (For more information about this behavior, see Handling Runtime Changes).
 * You want to avoid having to process all your images again so the user has a smooth
 * and fast experience when a configuration change occurs.
 * <p/>
 * Luckily, you have a nice memory cache of bitmaps that you built
 * in the Use a Memory Cache section.
 * This cache can be passed through to the new activity instance using a Fragment
 * which is preserved by calling setRetainInstance(true)).
 * After the activity has been recreated, this retained Fragment is reattached
 * and you gain access to the existing cache object, allowing images to be
 * quickly fetched and re-populated into the ImageView objects.
 */
/*
 * To test this out, try rotating a device both with and without retaining the Fragment.
 * You should notice little to no lag as the images populate the activity almost instantly
 * from memory when you retain the cache.
 * Any images not found in the memory cache are hopefully available in the disk cache,
 * if not, they are processed as usual.
 */
public class RetainFragment extends Fragment {

    private static final String LOG_TAG = "RetainFragment";
    public LruCache<String, Bitmap> mRetainedCache;

    public RetainFragment() {
    }

    public static RetainFragment findOrCreateRetainFragment(FragmentManager fm) {
        RetainFragment fragment = (RetainFragment) fm.findFragmentByTag(LOG_TAG);
        if (fragment == null) {
            fragment = new RetainFragment();
            fm.beginTransaction().add(fragment, LOG_TAG).commit();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}