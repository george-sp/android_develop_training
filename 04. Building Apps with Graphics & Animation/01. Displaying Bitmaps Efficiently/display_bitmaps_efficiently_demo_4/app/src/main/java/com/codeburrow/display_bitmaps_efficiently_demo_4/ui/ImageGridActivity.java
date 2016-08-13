package com.codeburrow.display_bitmaps_efficiently_demo_4.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.codeburrow.display_bitmaps_efficiently_demo_4.BuildConfig;

/**
 * Simple FragmentActivity to hold the main {@link ImageGridFragment} and not much else.
 */
public class ImageGridActivity extends FragmentActivity {

    private static final String LOG_TAG = "ImageGridActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            com.codeburrow.display_bitmaps_efficiently_demo_4.util.Utils.enableStrictMode();
        }
        super.onCreate(savedInstanceState);

        if (getSupportFragmentManager().findFragmentByTag(LOG_TAG) == null) {
            final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(android.R.id.content, new ImageGridFragment(), LOG_TAG);
            fragmentTransaction.commit();
        }
    }
}

