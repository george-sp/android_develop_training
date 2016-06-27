package com.codeburrow.sendfile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class DisplayImageActivity extends AppCompatActivity {

    private static final String LOG_TAG = DisplayImageActivity.class.getSimpleName();

    // ImageView to display the selected image file.
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        // Find the mImageView in the activity's layout.
        mImageView = (ImageView) findViewById(R.id.display_image_view);

        // Get intent's extras.
        String path = getIntent().getExtras().getString(FileSelectActivity.PATH_KEY);
        loadImageFromInternalStorage(path);
    }

    private void loadImageFromInternalStorage(String path) {
        try {
            File imageFile = new File(path);
            Bitmap imageBitmap = BitmapFactory.decodeStream(new FileInputStream(imageFile));

            mImageView.setImageBitmap(imageBitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
