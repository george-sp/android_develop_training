package com.codeburrow.requestfile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // The Request File Intent.
    private Intent mRequestFileIntent;
    // Parcel File Descriptor for the requested file.
    private ParcelFileDescriptor mInputPFD;
    // The content URI of the selected file.
    Uri returnUri;
    // ImageView to display the requested image.
    private ImageView mImageView;
    // TextView to display the returnUri.
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the mImageView.
        mImageView = (ImageView) findViewById(R.id.requested_image_view);
        // Find the mTextView.
        mTextView = (TextView) findViewById(R.id.uri_text_view);

        /**
         * To request a file from the server app, the client app calls
         * startActivityForResult()
         * with an Intent containing
         * - the action such as ACTION_PICK
         * - and a MIME type that the client app can handle.
         */
        mRequestFileIntent = new Intent(Intent.ACTION_PICK);
        mRequestFileIntent.setType("image/png");
    }

    protected void requestFile(){
        /**
         * When the user requests a file, send an Intent to the
         * server app.
         * files.
         */
        startActivityForResult(mRequestFileIntent, 0);
    }

    /*
     * When the Activity of the app that hosts files sets a result and calls finish(),
     * this method is invoked.
     * The returned Intent contains the content URI of a selected file.
     * The result code indicates if the selection worked or not.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent returnIntent) {
        // If the selection didn't work
        if (resultCode != RESULT_OK) {
            // Exit without doing anything else
            Toast.makeText(MainActivity.this, "The selection didn't work", Toast.LENGTH_SHORT).show();
            return;
        } else {
            // Get the file's content URI from the incoming Intent
            returnUri = returnIntent.getData();
            mTextView.setText(returnUri.toString());
            /*
             * Try to open the file for "read" access using the returned URI.
             * If the file isn't found, write to the error log and return.
             */
            try {
                /*
                 * Get the content resolver instance for this context,
                 * and use it to get a ParcelFileDescriptor for the file.
                 */
                mInputPFD = getContentResolver().openFileDescriptor(returnUri, "r");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("MainActivity", "File not found.");
                return;
            }
            /**
             * Get a regular file descriptor for the file.
             * The main practical use for a file descriptor is to create
             * a FileInputStream or FileOutputStream to contain it.
             */
            FileDescriptor fileDescriptor = mInputPFD.getFileDescriptor();

            // Get a bitmap image from the file descriptor.
            Bitmap requestedImageBitmap = BitmapFactory.decodeStream(new FileInputStream(fileDescriptor));
            // Display the requested image.
            mImageView.setImageBitmap(requestedImageBitmap);
        }
    }

    public void onRequestClick(View view) {
        requestFile();
    }
}
