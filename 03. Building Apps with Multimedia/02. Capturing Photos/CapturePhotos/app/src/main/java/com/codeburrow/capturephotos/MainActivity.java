package com.codeburrow.capturephotos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    // The request code for the take picture intent.
    static final int REQUEST_IMAGE_CAPTURE = 1;
    // Image View to display the picture captured by the user.
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.result_picture_image_view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            /*
             * The Android Camera application encodes the photo in the return Intent
             * delivered to onActivityResult() as a small Bitmap in the extras,
             * under the key "data".
             *
             * The following code retrieves this image and displays it in an ImageView.
             */
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }
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
