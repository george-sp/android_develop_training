package com.codeburrow.receivefileswithnfc;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // A File object containing the path to the transferred files
    private File mParentPath;
    // Incoming Intent
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called from onNewIntent() for a SINGLE_TOP Activity
     * or onCreate() for a new Activity.
     * <p/>
     * For onNewIntent(), remember to call setIntent() to store the most
     * current Intent.
     */
    private void handleViewIntent() {
        // Get the Intent action
        mIntent = getIntent();
        String action = mIntent.getAction();
        /*
         * For ACTION_VIEW, the Activity is being asked to display data.
         * Get the URI.
         */
        if (TextUtils.equals(action, Intent.ACTION_VIEW)) {
            // Get the URI from the Intent
            Uri beamUri = mIntent.getData();
            /*
             * Test for the type of URI, by getting its scheme value
             */
            if (TextUtils.equals(beamUri.getScheme(), "file")) {
                mParentPath = new File(handleFileUri(beamUri));
            } else if (TextUtils.equals(beamUri.getScheme(), "content")) {
                mParentPath = new File(handleContentUri(beamUri));
            }
        }
    }

    public String handleFileUri(Uri beamUri) {
        // Get the path part of the URI.
        String fileName = beamUri.getPath();
        // Create a File object for this filename.
        File copiedFile = new File(fileName);
        // Get a string containing the file's parent directory.
        return copiedFile.getParent();
    }

    /*
     * Android Beam file transfer indexes the media files it transfers by running Media Scanner on the directory where it stores transferred files.
     *
     * Media Scanner writes its results to the MediaStore content provider,
     * then it passes a content URI for the first file back to Android Beam file transfer.
     *
     * This content URI is the one you receive in the notification Intent.
     *
     * To get the directory of the first file, you retrieve it from MediaStore using the content URI.
     */
    private String handleContentUri(Uri beamUri) {
        // Position of the filename in the query Cursor.
        int filenameIndex;
        // File object for the filename.
        File copiedFile;
        // The filename stored in MediaStore.
        String fileName;
        /* Test the authority of the URI - Determine the content provider.
         *
         * To determine if you can retrieve a file directory from the content URI,
         * determine the the content provider associated with the URI by calling
         * Uri.getAuthority() to get the URI's authority.
         *
         * The result has two possible values:
         * - MediaStore.AUTHORITY
         * - Any other authority value
         */
        if (!TextUtils.equals(beamUri.getAuthority(), MediaStore.AUTHORITY)) {
            /*
             * Handle content URIs for other content providers
             */
        }
        // For a MediaStore content URI.
        else {
            // Get the column that contains the file name.
            String[] projection = {MediaStore.MediaColumns.DATA};
            Cursor pathCursor =
                    getContentResolver().query(beamUri, projection,
                            null, null, null);
            // Check for a valid cursor.
            if (pathCursor != null &&
                    pathCursor.moveToFirst()) {
                // Get the column index in the Cursor.
                filenameIndex = pathCursor.getColumnIndex(
                        MediaStore.MediaColumns.DATA);
                // Get the full file name including path.
                fileName = pathCursor.getString(filenameIndex);
                // Create a File object for the filename.
                copiedFile = new File(fileName);
                // Return the parent directory of the file.
                return copiedFile.getParent();
            } else {
                // The query didn't work; return null.
                return null;
            }
        }
        return null;
    }
}
