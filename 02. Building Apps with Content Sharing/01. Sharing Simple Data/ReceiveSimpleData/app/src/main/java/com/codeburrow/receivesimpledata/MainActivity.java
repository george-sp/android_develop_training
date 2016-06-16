package com.codeburrow.receivesimpledata;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private TextView mTextView;
    private ImageView mImageView_1;
    private ImageView mImageView_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get Views from the layout.
        mTextView = (TextView) findViewById(R.id.textview);
        mImageView_1 = (ImageView) findViewById(R.id.imageview_1);
        mImageView_2 = (ImageView) findViewById(R.id.imageview_2);

        // Get intent, action and MIME type.
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                // Handle text being sent.
                handleSendText(intent);
            } else if (type.startsWith("image/")) {
                // Handle single image being sent.
                handleSendImage(intent);
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                // Handle multiple images being sent.
                handleSendMultipleImages(intent);
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            mTextView.setText(sharedText);
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            // Update UI to reflect image being shared
            mImageView_1.setImageURI(imageUri);
        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
            mImageView_1.setImageURI(imageUris.get(0));
            mImageView_2.setImageURI(imageUris.get(1));
        }
    }
}
