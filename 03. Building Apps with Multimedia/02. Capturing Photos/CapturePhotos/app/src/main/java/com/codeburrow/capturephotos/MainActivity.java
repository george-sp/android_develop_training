package com.codeburrow.capturephotos;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    // The request code for the take picture intent.
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Helper Method.
     * <p/>
     * The Android way of delegating actions to other applications is
     * to invoke an Intent that describes what you want done.
     * <p/>
     * This process involves three pieces:
     * - the Intent itself
     * - a call to start the external Activity
     * - and some code to handle the image data when focus returns to your activity.
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            /*
             * Performing this check: resolveActivity() is important
             * because if you call startActivityForResult()
             * using an intent that no app can handle, your app will crash.
             */
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void takePictureButtonPressed(View view) {
        dispatchTakePictureIntent();
    }
}
