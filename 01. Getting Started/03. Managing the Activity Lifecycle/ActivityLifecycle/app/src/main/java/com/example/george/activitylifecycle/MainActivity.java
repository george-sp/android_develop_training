package com.example.george.activitylifecycle;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    /* Member variable for text view in the layout. */
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the user interface layout for this Activity.
        // The layout files is defined in the project res/layout/activity_main.xml file.
        setContentView(R.layout.activity_main);

        // Initialize member TextView so we can manipulate it later.
        mTextView = (TextView) findViewById(R.id.text_message);

        /*
         * Make sure we're running on Honeycomb or higher to use ActionBar APIs.
         *
         * Using the SDK_INT to prevent older systems from executing new APIs
         * works in this way on Android 2.0 (API level 5) and higher only.
         * Older versions will encounter a runtime exception.
         * */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // For the main activity, make sure the app icon in the action bar does not behave as a button.
            ActionBar actionBar = getActionBar();
            actionBar.setHomeButtonEnabled(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        android.os.Debug.startMethodTracing("activity_lifecycle");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();  // Always call the superclass

        // Stop method tracing that the activity started during onCreate()
        android.os.Debug.stopMethodTracing();
    }

}
