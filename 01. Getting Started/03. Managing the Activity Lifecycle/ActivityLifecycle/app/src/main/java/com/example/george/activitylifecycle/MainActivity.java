package com.example.george.activitylifecycle;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    Camera mCamera;

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

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onStart() {
        super.onStart();
        android.os.Debug.startMethodTracing("activity_lifecycle");
    }

    /*
     * The following example of onResume() is the counterpart to the onPause() example above,
     * so it initializes the camera that's released when the activity pauses.
     */
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        // Get the Camera instance as the activity achieves full user focus
        if (mCamera == null) {
            initializeCamera(); // Local method to handle camera init
        }
    }

    /*
     * If your application uses the Camera, the onPause() method is a good place to release it.
     */
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        // Release the Camera because we don't need it when paused
        // and other activities might need to use it.
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();  // Always call the superclass

        // Stop method tracing that the activity started during onCreate()
        android.os.Debug.stopMethodTracing();
    }

    private void initializeCamera() {
        mCamera = null;
        try {
            mCamera = Camera.open(0);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "No camera found" + "\n" + e.getMessage());
        }
    }

}
