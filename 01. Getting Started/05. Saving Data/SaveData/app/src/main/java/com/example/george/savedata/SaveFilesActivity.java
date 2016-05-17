package com.example.george.savedata;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since May/17/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
public class SaveFilesActivity extends AppCompatActivity {

    private static final String LOG_TAG = SaveFilesActivity.class.getSimpleName();

    File privateExternalFile;
    File publicExternalFile;

    EditText editText;

    String filename = "myfile";
    String albumName = "TEST_ALBUM";
    String albumNamePrivate = "TEST_ALBUM_PRIVATE";
    FileOutputStream fileOutputStream;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_files);

        editText = (EditText) findViewById(R.id.editText);

        /*
         * NOTE: Obtain Permissions for External Storage
         */

        getAlbumStoragePublicDir(albumName);
        // This directory is deleted when the app is uninstalled.
        getAlbumStoragePrivateDir(this, albumNamePrivate);


    }

    public void writeIntoFile(View view) {
        /*
         * getFilesDir():
         * Returns a File representing an internal directory for your app.
         *
         * getCacheDir()
         * Returns a File representing an internal directory for your app's temporary cache files.
         */
        try {
            File file = new File(getApplicationContext().getFilesDir(), filename);
            this.fileOutputStream = new FileOutputStream(file);
            // You can omit the lines above and comment in the one below.
//            this.fileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            String textToBeSaved = editText.getText().toString();
            this.fileOutputStream.write(textToBeSaved.getBytes());
            this.fileOutputStream.close();

            showMessage(file);
            Log.e(LOG_TAG, file.getAbsolutePath().toString() + ": " + textToBeSaved);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMessage(File file) {
        String message = "Current Available Space: " + readableFileSize(file.getFreeSpace()) + "\nTotal Space in Storage Volume: " + readableFileSize(file.getTotalSpace());
        Log.e(LOG_TAG, message);
        Toast.makeText(SaveFilesActivity.this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * SAMPLE CODE
     * <p/>
     * extracts the file name from a URL and creates a file with that name in your app's
     * internal cache directory
     *
     * @param context
     * @param url
     * @return a temporary file
     */
    public File getTempFile(Context context, String url) {
        File file = null;
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            file = File.createTempFile(fileName, null, context.getCacheDir());
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return file;
    }

    public void readFromFile(View view) {
        try {
            FileInputStream fileInputStream = openFileInput(filename);

            String readString = "";
            int charRead;

            while ((charRead = fileInputStream.read()) > 0) {
                // char to string conversion
                readString += Character.toString((char) charRead);
            }
            fileInputStream.close();
            Toast.makeText(getBaseContext(), readString, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Checks if external storage is available for read and write
     */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /*
     * Checks if external storage is available to at least read
     */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


    /**
     * @param albumName
     * @return
     */
    public File getAlbumStoragePublicDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        publicExternalFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!publicExternalFile.mkdirs()) {
            Log.e(LOG_TAG, "Public Directory not created");
        }
        return publicExternalFile;
    }

    /**
     * @param context
     * @param albumName
     * @return
     */
    public File getAlbumStoragePrivateDir(Context context, String albumName) {
        // Get the directory for the app's private pictures directory.
        privateExternalFile = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!privateExternalFile.mkdirs()) {
            Log.e(LOG_TAG, "Private Directory not created");
        }
        return privateExternalFile;
    }

    public void deleteAFile(View view) {
        /*
         * he most straightforward way to delete a file is to have the opened file reference
         * call delete() on itself:
         *                                          myFile.delete();
         *
         * If the file is saved on internal storage, you can also ask the Context to locate and
         * delete a file by calling deleteFile():
         *                                          myContext.deleteFile(fileName);
         */
        getApplicationContext().deleteFile(filename);
        publicExternalFile.delete();
        privateExternalFile.delete();
    }

    /**
     * Reference: http://stackoverflow.com/a/5599842
     *
     * @param size
     * @return a readable file size
     */
    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
