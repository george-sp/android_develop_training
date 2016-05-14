package com.example.george.activitylifecycle;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    Camera mCamera;

    /* Member variable for text view in the layout. */
    TextView mTextView;

    TextView countTextView;
    private static final String STATE_SCORE = "playerScore";
    private static final String STATE_LEVEL = "playerLevel";
    private int mCurrentScore = 0;
    private int mCurrentLevel = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Here's how you can restore some state data in onCreate()
        /*
        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            mCurrentScore = savedInstanceState.getInt(STATE_SCORE);
            mCurrentLevel = savedInstanceState.getInt(STATE_LEVEL);
        } else {
            // Probably initialize members with default values for a new instance
        }
        */

        // Set the user interface layout for this Activity.
        // The layout files is defined in the project res/layout/activity_main.xml file.
        setContentView(R.layout.activity_main);

        // Initialize member TextView so we can manipulate it later.
        mTextView = (TextView) findViewById(R.id.text_message);

        countTextView = (TextView) findViewById(R.id.text_count);

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                actionBar.setHomeButtonEnabled(false);
            }
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

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void initializeCamera() {
        mCamera = null;
        try {
            mCamera = Camera.open(0);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "No camera found" + "\n" + e.getMessage());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt(STATE_SCORE, mCurrentScore);
        savedInstanceState.putInt(STATE_LEVEL, mCurrentLevel);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    /*
     * Instead of restoring the state during onCreate() you may choose to implement
     * onRestoreInstanceState(), which the system calls after the onStart() method. The system
     * calls onRestoreInstanceState() only if there is a saved state to restore,
     * so you DO NOT
     * need to check whether the Bundle is null!
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        mCurrentScore = savedInstanceState.getInt(STATE_SCORE);
        mCurrentLevel = savedInstanceState.getInt(STATE_LEVEL);

        countTextView.setText(String.format("%d", mCurrentScore));
    }

    public void countScore(View view) {
        mCurrentScore += 1;
        countTextView.setText(String.format("%d", mCurrentScore));
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
