package com.codeburrow.sendfile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class FileSelectActivity extends AppCompatActivity {

    private static final String LOG_TAG = FileSelectActivity.class.getSimpleName();

    // The path to the root of this app's internal storage.
    private File mPrivateRootDir;
    // The path to the "images" subdirectory.
    private File mImagesDir;
    // Array of files in the images subdirectory.
    File[] mImageFiles;
    // ArrayList of filenames corresponding to mImageFiles.
    ArrayList<String> mImageFileNames;
    // Intent to send back to client-apps that request a file.
    Intent mResultIntent;
    // Content Uri for the selected file.
    Uri fileUri;
    // ListView to display the file names.
    private ListView mFileListView;
    // Bitmap images from res/drawable folder.
    Bitmap image_1, image_2, image_3, image_4;
    // PutExtra Key for the path of the selected file.
    public static final String PATH_KEY = "path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_select);

        // Find the mFileListView in the activity's layout.
        mFileListView = (ListView) findViewById(R.id.file_list_view);
        // Get images from drawable.
        getImagesFromDrawable();

        // Set up an Intent to send back to apps that request a file.
        mResultIntent = new Intent("com.example.myapp.ACTION_RETURN_FILE");
        // Get the files/ subdirectory of internal storage.
        mPrivateRootDir = getFilesDir();
        // Get the files/images subdirectory.
        mImagesDir = new File(mPrivateRootDir, "images");
        mImagesDir.mkdirs();
        // Save some images in the "images" subdirectory.
        saveImages();
        // Get the files in the images subdirectory.
        mImageFiles = mImagesDir.listFiles();
        // Set the Activity's result to null to begin with.
        setResult(Activity.RESULT_CANCELED, null);
        /*
         * Display the file names in the ListView mFileListView.
         *
         * Back the ListView with the array list mImageFileNames,
         * which you can create by iterating through mImageFiles
         * and calling File.getAbsolutePath() for each File.
         */
        mImageFileNames = new ArrayList<>();
        for (File imageFile : mImageFiles) {
            mImageFileNames.add(imageFile.getAbsolutePath());
        }
        mFileListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mImageFileNames));

        // Define a listener that responds to clicks on a file in the ListView
        mFileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /*
             * When a filename in the ListView is clicked, get its
             * content URI and send it to the requesting app
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*
                 * Get a File for the selected file name.
                 * Assume that the file names are in the
                 * mImageFilename array list.
                 */
                File requestFile = new File(mImageFileNames.get(position));
                /*
                 * Most file-related method calls need to be in
                 * try-catch blocks.
                 */
                // Use the FileProvider to get a content URI.
                try {
                    fileUri = FileProvider.getUriForFile(
                            FileSelectActivity.this,
                            "com.codeburrow.sendfile.fileprovider",
                            requestFile);

                    Log.e(LOG_TAG, fileUri.toString());
                } catch (IllegalArgumentException e) {
                    Log.e(LOG_TAG, "File Selector: The selected file can't be shared: " + mImageFileNames.get(position) + "\n" + e.getMessage());
                }
                /*
                 * CAUTION:
                 * Calling setFlags() is the only way to securely grant access to your files using temporary access permissions.
                 * Avoid calling Context.grantUriPermission() method for a file's content URI,
                 * since this method grants access that you can only revoke by calling Context.revokeUriPermission().
                 */
                if (fileUri != null) {
                    // Grant temporary read permission to the content URI
                    mResultIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    // Put the Uri and MIME type in the result Intent
                    mResultIntent.setDataAndType(
                            fileUri,
                            getContentResolver().getType(fileUri));
                    // Set the result
                    FileSelectActivity.this.setResult(Activity.RESULT_OK, mResultIntent);
                } else {
                    mResultIntent.setDataAndType(null, "");
                    FileSelectActivity.this.setResult(RESULT_CANCELED, mResultIntent);
                }
            }
        });

        // Define a listener that responds to long clicks on a file in the ListView
        mFileListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent displayImageIntent = new Intent(FileSelectActivity.this, DisplayImageActivity.class);
                displayImageIntent.putExtra(PATH_KEY, mImageFileNames.get(position));
                startActivity(displayImageIntent);
                return true;
            }
        });
    }

    /**
     * HELPER METHOD
     * <p/>
     * Save a bitmap image to the app's internal storage.
     *
     * @param image The Bitmap Image to be saved
     * @return
     */
    public boolean saveImageToInternalStorage(Bitmap image, String imageFileName) {
        try {
            File fileImage_1 = new File(mImagesDir, imageFileName);
            // Use the compress method on the Bitmap object to write image to the output stream.
            FileOutputStream fileOutputStream = new FileOutputStream(fileImage_1);
            // Writing the bitmap to the output stream.
            image.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            Log.e(LOG_TAG, "saveToInternalStorage(): " + e.getMessage());
            return false;
        }
    }

    private void saveImages() {
        saveImageToInternalStorage(image_1, "android.png");
        saveImageToInternalStorage(image_2, "android_black.png");
        saveImageToInternalStorage(image_3, "android_blue.png");
        saveImageToInternalStorage(image_4, "android_security.png");
    }

    /**
     * HELPER METHOD
     * <p/>
     * Get image files from drawable folder.
     *
     * @return
     */
    public void getImagesFromDrawable() {
        image_1 = BitmapFactory.decodeResource(getResources(), R.drawable.android);
        image_2 = BitmapFactory.decodeResource(getResources(), R.drawable.android_black);
        image_3 = BitmapFactory.decodeResource(getResources(), R.drawable.android_blue);
        image_4 = BitmapFactory.decodeResource(getResources(), R.drawable.android_security);
    }

    /**
     * Provide users with a way to return immediately to the client app once they have chosen a file.
     *
     * @param view The "Done" Button
     */
    public void onDoneClick(View view) {
        // Associate a method with the Done button
        finish();
    }
}
