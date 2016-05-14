package com.example.george.activitylifecycle;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.location.LocationManager;
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
     *Here's an implementation of onStop() that saves the contents of a draft note to persistent storage:
     */
    @Override
    protected void onStop() {
        super.onStop();  // Always call the superclass method first

        // Save the note's current draft, because the activity is stopping
        // and we want to be sure the current note progress isn't lost.
     /*
        ContentValues values = new ContentValues();
        values.put(NotePad.Notes.COLUMN_NAME_NOTE, getCurrentNoteText());
        values.put(NotePad.Notes.COLUMN_NAME_TITLE, getCurrentNoteTitle());

        getContentResolver().update(
                mUri,    // The URI for the note to update.
                values,  // The map of column names and new values to apply to them.
                null,    // No SELECT criteria are used.
                null     // No WHERE columns are used.
        );
       */
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


    /*
    @Override
    protected void onStart() {
        super.onStart();  // Always call the superclass method first

        // The activity is either being restarted or started for the first time
        // so this is where we should make sure that GPS is enabled
        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
            // Create a dialog here that requests the user to enable GPS, and use an intent
            // with the android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS action
            // to take the user to the Settings screen to enable GPS when they click "OK"
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first

        // Activity being restarted from stopped state
    }
    */
}
