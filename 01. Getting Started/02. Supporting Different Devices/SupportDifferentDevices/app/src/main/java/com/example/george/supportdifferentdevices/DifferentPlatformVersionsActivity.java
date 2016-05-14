package com.example.george.supportdifferentdevices;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class DifferentPlatformVersionsActivity extends AppCompatActivity {

    public static final String LOG_TAG = DifferentPlatformVersionsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_different_platform_versions);

        setUpActionBar();
    }

    private void setUpActionBar() {
        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);

            Log.i(LOG_TAG, "we're running on Honeycomb or higher");
        } else {
            Log.i(LOG_TAG, "we're running on lower than Honeycomb");
        }
    }

}
